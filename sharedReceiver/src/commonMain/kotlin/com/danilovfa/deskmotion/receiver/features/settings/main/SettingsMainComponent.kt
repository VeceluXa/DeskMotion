package com.danilovfa.deskmotion.receiver.features.settings.main

import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore
import kotlinx.coroutines.flow.StateFlow

interface SettingsMainComponent {
    val state: StateFlow<SettingsMainStore.State>

    fun onEvent(event: SettingsMainStore.Intent)

    sealed class Output {
        data object Restart : Output()
    }
}