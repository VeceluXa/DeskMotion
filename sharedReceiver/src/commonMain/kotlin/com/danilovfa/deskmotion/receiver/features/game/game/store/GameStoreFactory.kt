package com.danilovfa.deskmotion.receiver.features.game.game.store

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
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_DELAY
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_HEIGHT
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_WIDTH
import com.danilovfa.deskmotion.receiver.utils.Constants.SCORE_INCREMENT
import com.danilovfa.deskmotion.receiver.utils.Constants.SPEED_MULTIPLIER
import com.danilovfa.deskmotion.receiver.utils.Constants.TARGET_SCORE_BOUND
import com.danilovfa.deskmotion.utils.time.currentTime
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class GameStoreFactory(
    private val storeFactory: StoreFactory,
    private val level: Level,
    private val connectionData: ConnectionData
) : KoinComponent {

    var dataServer: DataServer? = null

    val socketManager: SocketManager by inject { parametersOf(true) }
    val repository: DeskMotionRepository by inject()

    fun create(): GameStore = object : GameStore,
        Store<GameStore.Intent, GameStore.State, GameStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = GameStore.State(),
            bootstrapper = SimpleBootstrapper(
                Action.SetTargets(level.targets),
                Action.StartGameLoop,
                Action.StartConnection
            ),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data object ShowCloseDialog : Msg()
        data object HideCloseDialog : Msg()
        data class ShowErrorDialog(val message: String) : Msg()
        data object HideErrorDialog : Msg()
        data class AddEvent(val event: TransferEvent) : Msg()
        data class UpdateCursorPosition(val cursorPosition: Coordinate) : Msg()
        data object RemoveFirstTarget : Msg()
        data object UpdateScore : Msg()
        data class UpdateTargets(val targets: List<Coordinate>) : Msg()
        data class UpdateTimer(val millis: Long) : Msg()
    }

    private sealed class Action {
        data class SetTargets(val targets: List<Coordinate>) : Action()
        data object StartConnection : Action()
        data object StartGameLoop : Action()
        data object EndGame : Action()
        data class HandleDataEvent(val event: TransferEvent.Data, val millis: Long) : Action()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<GameStore.Intent, Action, GameStore.State, Msg, GameStore.Label>(
            Dispatchers.Main
        ) {

        private var readDataJob: Job? = null
        private var lastReceivedEvent: TransferEvent? = null

        override fun executeAction(action: Action, getState: () -> GameStore.State) =
            when (action) {
                Action.StartGameLoop -> startGame()
                Action.StartConnection -> connect()
                is Action.HandleDataEvent -> handleDataEvent(
                    action.event,
                    action.millis,
                    getState().cursor,
                    getState().targets
                )

                Action.EndGame -> endGame(getState().score, getState().log)
                is Action.SetTargets -> dispatch(Msg.UpdateTargets(action.targets))
            }

        override fun executeIntent(intent: GameStore.Intent, getState: () -> GameStore.State) =
            when (intent) {
                GameStore.Intent.OnBackClicked -> dispatch(Msg.ShowCloseDialog)
                GameStore.Intent.ConfirmCloseDialog -> navigateBack()
                GameStore.Intent.DismissCloseDialog -> dispatch(Msg.HideCloseDialog)
                GameStore.Intent.DismissErrorDialog -> navigateBack()
            }

        private fun connect() {
            val handler = CoroutineExceptionHandler { _, error ->
                Logger.e(DataServer.TAG, error)
                dispatch(Msg.ShowErrorDialog(error.message ?: ""))
            }
            scope.launch(handler) {
                dataServer = when (connectionData) {
                    ConnectionData.Bluetooth -> null
                    is ConnectionData.Wifi -> socketManager.also {
                        socketManager.connect(connectionData.ip, connectionData.port)
                    }
                }
                observeReceivedData()
            }
        }

        private fun observeReceivedData() {
            dataServer?.let { dataServer ->
                readDataJob = dataServer.receiveData().onEach {
//                    Logger.d(DataServer.TAG) { it.toString() }
                    lastReceivedEvent = it
                    dispatch(Msg.AddEvent(it))
                }.launchIn(scope)
            }
        }

        private fun navigateBack() {
            readDataJob?.cancel()
            scope.launch {
                dataServer?.disconnect()
            }
            dispatch(Msg.HideErrorDialog)
            publish(GameStore.Label.NavigateBack)
        }

        private fun startGame() {
            scope.launch {
                var millis = 0L
                while (true) {
                    delay(GAME_DELAY)
                    lastReceivedEvent?.let { event ->
                        millis += GAME_DELAY
                        dispatch(Msg.UpdateTimer(millis))
                        when (event) {
                            is TransferEvent.Data -> executeAction(
                                Action.HandleDataEvent(
                                    event, millis
                                )
                            )

                            is TransferEvent.End -> executeAction(Action.EndGame)
                            is TransferEvent.Start -> Unit
                        }
                    }
                }
            }
        }

        private fun endGame(score: Int, log: List<Coordinate>) {
            readDataJob?.cancel()

            val playLog = PlayLog(
                levelId = level.id,
                log = log,
                score = score,
                completedEpochMillis = currentTime()
            )

            scope.launch {
                publish(GameStore.Label.EndGame(playLog))
            }
        }

        private fun handleDataEvent(
            event: TransferEvent.Data,
            millis: Long,
            cursorPosition: Coordinate,
            targets: List<Coordinate>
        ) {
            if (targets.isEmpty()) {
                executeAction(Action.EndGame)
            } else if (targets.first().millis < millis) {
                dispatch(Msg.RemoveFirstTarget)
            } else {
                val target = targets.first()
                updateCursorPosition(event, cursorPosition)
                updateScore(cursorPosition, target)
            }
        }

        private fun updateCursorPosition(
            dataEvent: TransferEvent.Data, cursorPosition: Coordinate
        ) {
            val x = (cursorPosition.x - dataEvent.dx * SPEED_MULTIPLIER).roundToInt()
            val y = (cursorPosition.y + dataEvent.dy * SPEED_MULTIPLIER).roundToInt()

            val newCursorPosition = cursorPosition.copy(
                x = if (x in 0..GAME_SCREEN_WIDTH) x else cursorPosition.x,
                y = if (y in 0..GAME_SCREEN_HEIGHT) y else cursorPosition.y,
                millis = currentTime()
            )

            dispatch(Msg.UpdateCursorPosition(newCursorPosition))
        }

        private fun updateScore(
            cursorPosition: Coordinate,
            target: Coordinate,
        ) {
            if (
                cursorPosition.x in target.x - TARGET_SCORE_BOUND..target.x + TARGET_SCORE_BOUND &&
                cursorPosition.y in target.y - TARGET_SCORE_BOUND..target.y + TARGET_SCORE_BOUND
            ) {
                dispatch(Msg.UpdateScore)
            }
        }
    }

    private object ReducerImpl : Reducer<GameStore.State, Msg> {
        override fun GameStore.State.reduce(msg: Msg): GameStore.State = when (msg) {
            Msg.HideCloseDialog -> copy(isCloseDialogOpen = false)
            Msg.ShowCloseDialog -> copy(isCloseDialogOpen = true)
            Msg.HideErrorDialog -> copy(isErrorDialogOpen = false, errorMessage = "")
            is Msg.ShowErrorDialog -> copy(isErrorDialogOpen = true, errorMessage = msg.message)
            is Msg.AddEvent -> copy(lastReceivedEvent = msg.event)
            is Msg.UpdateCursorPosition -> {
                val newLog = log.toMutableList().also {
                    it.add(msg.cursorPosition)
                }

                copy(cursor = msg.cursorPosition, log = newLog)
            }

            Msg.RemoveFirstTarget -> {
                val newTargets = targets.toMutableList().also {
                    it.removeAt(0)
                }

                copy(targets = newTargets)
            }

            Msg.UpdateScore -> copy(score = score + SCORE_INCREMENT)
            is Msg.UpdateTargets -> copy(targets = msg.targets)
            is Msg.UpdateTimer -> copy(millis = msg.millis)
        }

    }

    companion object {
        const val STORE_NAME = "GameReceiverStore"
    }
}