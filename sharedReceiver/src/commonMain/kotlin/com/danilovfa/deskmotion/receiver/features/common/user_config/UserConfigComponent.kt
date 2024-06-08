package com.danilovfa.deskmotion.receiver.features.common.user_config

import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore
import kotlinx.coroutines.flow.StateFlow

interface UserConfigComponent {
    val stateFlow: StateFlow<UserConfigStore.State>

    fun onIntent(intent: UserConfigStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data object NavigateNext : Output()
    }
}