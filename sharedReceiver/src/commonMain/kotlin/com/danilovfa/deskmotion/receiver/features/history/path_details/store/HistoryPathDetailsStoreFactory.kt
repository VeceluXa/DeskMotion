package com.danilovfa.deskmotion.receiver.features.history.path_details.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryPathDetailsStoreFactory(
    private val storeFactory: StoreFactory,
    private val logId: Long
) : KoinComponent {
    private val repository: DeskMotionRepository by inject()

    fun create(): HistoryPathDetailsStore = object : HistoryPathDetailsStore,
        Store<HistoryPathDetailsStore.Intent, HistoryPathDetailsStore.State, HistoryPathDetailsStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = HistoryPathDetailsStore.State(),
            bootstrapper = SimpleBootstrapper(Action.LoadLog),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Action {
        data object LoadLog : Action()
    }

    sealed class Msg {
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
        data class ShowLogs(val logs: List<Coordinate>) : Msg()
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<HistoryPathDetailsStore.Intent, Action, HistoryPathDetailsStore.State, Msg, HistoryPathDetailsStore.Label>(
            Dispatchers.Main
        ) {
        override fun executeAction(action: Action, getState: () -> HistoryPathDetailsStore.State) =
            when (action) {
                Action.LoadLog -> loadLog()
            }

        override fun executeIntent(
            intent: HistoryPathDetailsStore.Intent,
            getState: () -> HistoryPathDetailsStore.State
        ) = when (intent) {
            HistoryPathDetailsStore.Intent.OnBackClicked -> publish(HistoryPathDetailsStore.Label.NavigateBack)
            HistoryPathDetailsStore.Intent.DismissErrorDialog -> publish(HistoryPathDetailsStore.Label.NavigateBack)
        }

        private fun loadLog() {
            lceFlow {
                emit(repository.getLog(logId))
            }
                .onEachLoading { dispatch(Msg.ShowLoading) }
                .onEachContent { logs ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.ShowLogs(logs.log))
                }
                .onEachError { error ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<HistoryPathDetailsStore.State, Msg> {
        override fun HistoryPathDetailsStore.State.reduce(msg: Msg): HistoryPathDetailsStore.State =
            when (msg) {
                Msg.ShowLoading -> copy(isLoading = true)
                Msg.HideLoading -> copy(isLoading = false)
                is Msg.ShowError -> copy(isError = true, errorMessage = "")
                Msg.HideError -> copy(isError = false, errorMessage = "")
                is Msg.ShowLogs -> copy(logs = msg.logs)
            }
    }

    companion object {
        const val STORE_NAME = "HistoryPathDetailsStore"
    }

}