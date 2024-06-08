package com.danilovfa.deskmotion.receiver.features.game.configuration.user

import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore
import kotlinx.coroutines.flow.StateFlow

interface UserConfigComponent {
    val stateFlow: StateFlow<UserConfigStore.State>

    fun onIntent(intent: UserConfigStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data class NavigateNext(val level: Level) : Output()
    }
}