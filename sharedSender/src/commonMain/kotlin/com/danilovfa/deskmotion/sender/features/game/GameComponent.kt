package com.danilovfa.deskmotion.sender.features.game

import com.danilovfa.deskmotion.sender.features.game.store.GameStore
import kotlinx.coroutines.flow.StateFlow

interface GameComponent {
    val state: StateFlow<GameStore.State>

    fun onEvent(event: GameStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
    }
}