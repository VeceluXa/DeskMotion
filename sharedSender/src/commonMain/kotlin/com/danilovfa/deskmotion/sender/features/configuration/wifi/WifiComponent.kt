package com.danilovfa.deskmotion.sender.features.configuration.wifi

import com.danilovfa.deskmotion.sender.features.configuration.wifi.store.WifiStore
import kotlinx.coroutines.flow.StateFlow

interface WifiComponent {
    val state: StateFlow<WifiStore.State>
    fun onEvent(event: WifiStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data class NavigateToGame(val ip: String, val port: Int) : Output()
    }
}