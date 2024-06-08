package com.danilovfa.deskmotion.receiver.features.history.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.mapToLce
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryMainStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    val repository: DeskMotionRepository by inject()

    fun create(): HistoryMainStore = object : HistoryMainStore,
        Store<HistoryMainStore.Intent, HistoryMainStore.State, HistoryMainStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = HistoryMainStore.State(),
            bootstrapper = SimpleBootstrapper(Action.LoadLogs),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Action {
        data object LoadLogs : Action()
    }

    sealed class Msg {
        data class ShowLogs(val logs: List<PlayLog>) : Msg()
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
        data class UpdateIsTheOnlyChild(val isTheOnlyChild: Boolean) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<HistoryMainStore.Intent, Action, HistoryMainStore.State, Msg, HistoryMainStore.Label>(
            Dispatchers.Main
        ) {
        private var loadLogsJob: Job? = null

        override fun executeAction(action: Action, getState: () -> HistoryMainStore.State) =
            when (action) {
                Action.LoadLogs -> loadLogs()
            }

        override fun executeIntent(
            intent: HistoryMainStore.Intent,
            getState: () -> HistoryMainStore.State
        ) = when (intent) {
            is HistoryMainStore.Intent.OnPlayLogClicked -> publish(
                HistoryMainStore.Label.OnPlayLogClicked(
                    intent.log
                )
            )

            HistoryMainStore.Intent.DismissErrorDialog -> dismissErrorDialog()
        }

        private fun loadLogs() {
            loadLogsJob?.cancel()
            loadLogsJob = repository
                .getLogs()
                .mapToLce()
                .onEachLoading {
                    dispatch(Msg.ShowLoading)
                }
                .onEachContent { playLogs ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.ShowLogs(playLogs))
                    checkIfTheOnlyChild(playLogs)
                }
                .onEachError { dispatch(Msg.ShowError(it.message ?: "")) }
                .launchIn(scope)
        }

        private fun checkIfTheOnlyChild(playLogs: List<PlayLog>) {
            if (playLogs.isEmpty()) return
            var isTheOnlyChild = true

            val iterator = playLogs.iterator()
            var prevItem = iterator.next()
            while (iterator.hasNext() && isTheOnlyChild) {
                val currItem = iterator.next()
//                if (prevItem.firstName != currItem.firstName ||
//                    prevItem.lastName != currItem.lastName ||
//                    prevItem.middleName != currItem.middleName) {
//                    isTheOnlyChild = false
//                }

                prevItem = currItem
            }

            dispatch(Msg.UpdateIsTheOnlyChild(isTheOnlyChild))
        }

        private fun dismissErrorDialog() {
            dispatch(Msg.HideError)
            loadLogs()
        }
    }

    private object ReducerImpl : Reducer<HistoryMainStore.State, Msg> {
        override fun HistoryMainStore.State.reduce(msg: Msg): HistoryMainStore.State = when (msg) {
            is Msg.ShowLogs -> copy(logs = msg.logs, isLoading = false)
            is Msg.ShowError -> copy(isError = true, errorMessage = msg.message)
            Msg.HideError -> copy(isError = false, errorMessage = "")
            Msg.HideLoading -> copy(isLoading = false)
            Msg.ShowLoading -> copy(isLoading = true)
            is Msg.UpdateIsTheOnlyChild -> copy(isTheOnlyChild = msg.isTheOnlyChild)
        }
    }

    companion object {
        const val STORE_NAME = "HistoryMainStore"
    }
}