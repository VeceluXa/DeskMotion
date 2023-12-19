package com.danilovfa.deskmotion.receiver.features.game.game

import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.game.game.store.GameStore
import kotlinx.coroutines.flow.StateFlow

interface GameComponent {

    val state: StateFlow<GameStore.State>

    fun onEvent(event: GameStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data class EndGame(val log: PlayLog) : Output()
    }
}