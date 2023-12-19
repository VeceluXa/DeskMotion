package com.danilovfa.deskmotion.receiver.features.game.finish.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameFinishStoreFactory(
    private val storeFactory: StoreFactory, private val logs: PlayLog
) : KoinComponent {
    val repository: DeskMotionRepository by inject()

    fun create(): GameFinishStore = object : GameFinishStore,
        Store<GameFinishStore.Intent, GameFinishStore.State, GameFinishStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = GameFinishStore.State(playLog = logs),
            bootstrapper = null,
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Msg {
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<GameFinishStore.Intent, Nothing, GameFinishStore.State, Msg, GameFinishStore.Label>(
            Dispatchers.Main
        ) {
        override fun executeIntent(
            intent: GameFinishStore.Intent, getState: () -> GameFinishStore.State
        ) = when (intent) {
            GameFinishStore.Intent.OnOkClicked -> saveLogs()
            GameFinishStore.Intent.DismissErrorDialog -> dispatch(Msg.HideError)
        }

        private fun saveLogs() {
            lceFlow {
                emit(repository.addLog(logs))
            }
                .onEachLoading { dispatch(Msg.ShowLoading) }
                .onEachContent {
                    dispatch(Msg.HideLoading)
                    publish(GameFinishStore.Label.NavigateBack)
                }
                .onEachError { dispatch(Msg.ShowError(it.message ?: "")) }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<GameFinishStore.State, Msg> {
        override fun GameFinishStore.State.reduce(msg: Msg): GameFinishStore.State = when (msg) {
            Msg.ShowLoading -> copy(isLoading = true)
            Msg.HideLoading -> copy(isLoading = false)
            is Msg.ShowError -> copy(isLoading = false, isError = true, errorMessage = msg.message)
            Msg.HideError -> copy(isError = false, errorMessage = "")
        }

    }

    companion object {
        const val STORE_NAME = "GameFinishStore"
    }
}