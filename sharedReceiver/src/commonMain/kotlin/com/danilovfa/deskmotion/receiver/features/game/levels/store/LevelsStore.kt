package com.danilovfa.deskmotion.receiver.features.game.levels.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.Level

interface LevelsStore : Store<LevelsStore.Intent, LevelsStore.State, LevelsStore.Label> {
    sealed class Intent {
        data class OnLevelClicked(val level: Level) : Intent()
    }

    data class State(
        val levels: List<Level> = emptyList(),
        val isLoading: Boolean = true,
    )

    sealed class Label {
        data class LevelSelected(val level: Level) : Label()
    }
}