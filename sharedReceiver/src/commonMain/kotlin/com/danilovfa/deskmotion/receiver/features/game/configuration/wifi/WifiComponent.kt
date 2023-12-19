package com.danilovfa.deskmotion.receiver.features.game.configuration.wifi

import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.store.WifiStore
import kotlinx.coroutines.flow.StateFlow

interface WifiComponent {
    val state: StateFlow<WifiStore.State>
    fun onEvent(event: WifiStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data class NavigateToGame(val level: Level, val ip: String, val port: Int) : Output()
    }
}