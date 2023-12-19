package com.danilovfa.deskmotion.sender.features.game.store

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.connection.ConnectionData
import com.danilovfa.deskmotion.library.connection.DataServer
import com.danilovfa.deskmotion.library.connection.socket.SocketManager
import com.danilovfa.deskmotion.model.TransferEvent
import com.danilovfa.deskmotion.sender.library.sensors.Accelerometer
import com.danilovfa.deskmotion.utils.time.currentTime
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class GameStoreFactory(
    private val storeFactory: StoreFactory,
    private val connectionData: ConnectionData
) : KoinComponent {

    var dataServer: DataServer? = null
    val socketManager: SocketManager by inject { parametersOf(false) }

    private val accelerometer: Accelerometer by inject()

    fun create(): GameStore = object : GameStore,
        Store<GameStore.Intent, GameStore.State, GameStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = GameStore.State(),
            bootstrapper = SimpleBootstrapper(Action.StartConnection, Action.StartSensor),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class AddEvent(val event: TransferEvent) : Msg()
        data class ShowError(val message: String) : Msg()
        data object HideErrorDialog : Msg()
        data object ShowCloseDialog : Msg()
        data object HideCloseDialog : Msg()
    }

    private sealed class Action {
        data object StartConnection : Action()
        data object StartSensor : Action()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<GameStore.Intent, Action, GameStore.State, Msg, GameStore.Label>(
            Dispatchers.Main
        ) {
        private var accelerometerJob: Job? = null
        private var readDataJob: Job? = null

        override fun executeAction(action: Action, getState: () -> GameStore.State) =
            when (action) {
                Action.StartConnection -> connect()
                Action.StartSensor -> startSensor()
            }

        override fun executeIntent(intent: GameStore.Intent, getState: () -> GameStore.State) =
            when (intent) {
                GameStore.Intent.OnBackButtonClicked -> dispatch(Msg.ShowCloseDialog)
                GameStore.Intent.DismissErrorDialog -> navigateBack()
                GameStore.Intent.ConfirmCloseDialog -> navigateBack()
                GameStore.Intent.DismissCloseDialog -> dispatch(Msg.HideCloseDialog)
            }

        private fun connect() {
            val handler = CoroutineExceptionHandler { _, error ->
                Logger.e(DataServer.TAG, error)
                dispatch(Msg.ShowError(error.message ?: ""))
            }
            scope.launch(handler) {
                dataServer = when (connectionData) {
                    ConnectionData.Bluetooth -> null
                    is ConnectionData.Wifi -> socketManager.also { socketManager ->
                        socketManager.connect(connectionData.ip, connectionData.port)
                    }
                }
            }
        }

        private fun navigateBack() {
            accelerometerJob?.cancel()
            readDataJob?.cancel()
            accelerometer.stopListening()
            scope.launch {
                sendEvent(TransferEvent.End(currentTime()))
                dataServer?.disconnect()
            }
            dispatch(Msg.HideErrorDialog)
            publish(GameStore.Label.NavigateBack)
        }

        private fun startSensor() {
            accelerometer.startListening()
            accelerometerJob = accelerometer.accelerometerFlow
                .onEach { data ->
                    data?.let { accelerometerData ->
                        val event = accelerometerData.toTransferEvent()
                        sendEvent(event)
                    }
                }
                .launchIn(scope)
        }

        private suspend fun sendEvent(event: TransferEvent) {
            try {
                dataServer?.sendData(event)
                dispatch(Msg.AddEvent(event))
            } catch (e: Exception) {
                dispatch(Msg.ShowError(e.message ?: ""))
            }
        }
    }

    private object ReducerImpl : Reducer<GameStore.State, Msg> {
        override fun GameStore.State.reduce(msg: Msg): GameStore.State = when (msg) {
            is Msg.AddEvent -> {
                val newLogs = logs.toMutableList()
                newLogs.add(0, msg.event)
                copy(logs = newLogs)
            }

            is Msg.ShowError -> {
                copy(isErrorDialogOpen = true, errorMessage = msg.message)
            }

            Msg.HideErrorDialog -> {
                copy(isErrorDialogOpen = false, errorMessage = "")
            }

            Msg.HideCloseDialog -> {
                copy(isCloseDialogOpen = false)
            }

            Msg.ShowCloseDialog -> {
                copy(isCloseDialogOpen = true)
            }
        }
    }

    companion object {
        private const val STORE_NAME = "GameSenderStore"
    }
}