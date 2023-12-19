package com.danilovfa.deskmotion.receiver.features.game.levels.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.defaultLevels
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LevelsStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {

    private val repository by inject<DeskMotionRepository>()

    fun create(): LevelsStore = object : LevelsStore,
        Store<LevelsStore.Intent, LevelsStore.State, LevelsStore.Label> by storeFactory.create(
        name = STORE_NAME,
        initialState = LevelsStore.State(),
        bootstrapper = SimpleBootstrapper(Action.LoadLevels),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    sealed class Action {
        data object LoadLevels : Action()
    }

    sealed class Msg {
        data class AddLevels(val levels: List<Level>) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<LevelsStore.Intent, Action, LevelsStore.State, Msg, LevelsStore.Label>(
            Dispatchers.Main
        ) {
        override fun executeAction(action: Action, getState: () -> LevelsStore.State) = when (action) {
            Action.LoadLevels -> loadLevels()
        }

        override fun executeIntent(intent: LevelsStore.Intent, getState: () -> LevelsStore.State) = when (intent) {
            is LevelsStore.Intent.OnLevelClicked -> publish(LevelsStore.Label.LevelSelected(intent.level))
        }

        private fun loadLevels() {
            scope.launch {
                var levels = repository.getLevels()
                if (levels.isEmpty()) {
                    defaultLevels.forEach { repository.addLevel(it) }
                    levels = repository.getLevels()
                }

                dispatch(Msg.AddLevels(levels))
            }
        }
    }


    private object ReducerImpl : Reducer<LevelsStore.State, Msg> {
        override fun LevelsStore.State.reduce(msg: Msg): LevelsStore.State = when (msg) {
            is Msg.AddLevels -> copy(levels = msg.levels)
        }
    }

    companion object {
        const val STORE_NAME = "LevelsStore"
    }
}