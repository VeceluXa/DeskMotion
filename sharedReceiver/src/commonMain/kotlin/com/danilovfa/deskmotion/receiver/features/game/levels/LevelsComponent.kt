package com.danilovfa.deskmotion.receiver.features.game.levels

import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore
import com.danilovfa.deskmotion.receiver.domain.model.Level
import kotlinx.coroutines.flow.StateFlow

interface LevelsComponent {

    val state: StateFlow<LevelsStore.State>

    fun onEvent(event: LevelsStore.Intent)

    sealed class Output {
        data class LevelSelected(val level: Level) : Output()
    }
}