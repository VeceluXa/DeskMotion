package com.danilovfa.deskmotion.receiver.features.game.finish

import com.danilovfa.deskmotion.receiver.features.game.finish.store.GameFinishStore
import kotlinx.coroutines.flow.StateFlow

interface GameFinishComponent {
    val state: StateFlow<GameFinishStore.State>

    fun onEvent(event: GameFinishStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
    }
}