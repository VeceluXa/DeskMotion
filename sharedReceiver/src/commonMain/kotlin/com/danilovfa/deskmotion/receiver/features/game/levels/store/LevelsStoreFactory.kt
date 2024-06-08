package com.danilovfa.deskmotion.receiver.features.game.levels.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.mapToLce
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.defaultLevels
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore.Intent
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore.Label
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LevelsStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val repository by inject<DeskMotionRepository>()

    fun create(): LevelsStore = object : LevelsStore,
        Store<Intent, State, Label> by storeFactory.create(
        name = STORE_NAME,
        initialState = State(),
        bootstrapper = SimpleBootstrapper(Action.LoadLevels),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    sealed class Action {
        data object LoadLevels : Action()
    }

    sealed class Msg {
        data class AddLevels(val levels: List<Level>) : Msg()
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Msg, Label>(
            Dispatchers.Main
        ) {
        override fun executeAction(action: Action, getState: () -> State) = when (action) {
            Action.LoadLevels -> loadLevels()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            is Intent.OnLevelClicked -> publish(Label.LevelSelected(intent.level))
        }

        private fun loadLevels() {
            lceFlow {
                var levels = repository.getLevels()

                if (levels.isEmpty()) {
                    defaultLevels.forEach { repository.addLevel(it) }
                    levels = repository.getLevels()
                }

                emit(levels)
            }
                .onEachLoading {
                    dispatch(Msg.ShowLoading)
                }
                .onEachContent { levels ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.AddLevels(levels))
                }
                .onEachError {
                    dispatch(Msg.HideLoading)
                }
                .launchIn(scope)
        }
    }


    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.AddLevels -> copy(levels = msg.levels)
            Msg.HideLoading -> copy(isLoading = false)
            Msg.ShowLoading -> copy(isLoading = true)
        }
    }

    companion object {
        const val STORE_NAME = "LevelsStore"
    }
}