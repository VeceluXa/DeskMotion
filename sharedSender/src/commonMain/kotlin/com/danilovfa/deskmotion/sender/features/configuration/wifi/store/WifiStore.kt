package com.danilovfa.deskmotion.sender.features.configuration.wifi.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.utils.net.getLocalIPAddress
import com.danilovfa.deskmotion.utils.validators.isIpValid
import com.danilovfa.deskmotion.utils.validators.isPortValid

interface WifiStore : Store<WifiStore.Intent, WifiStore.State, WifiStore.Label> {
    sealed class Intent {
        data class OnIpChanged(val ip: String) : Intent()
        data class OnPortChanged(val port: String) : Intent()
        data object OnStartClicked : Intent()
        data object OnBackClicked : Intent()
    }

    data class State(
        val ip: String = "",
        val port: String = "",
        val isIpError: Boolean = false,
        val isPortError: Boolean = false,
        val localIpAddress: String? = getLocalIPAddress()
    ) {
        val isButtonEnabled get() = isIpValid(ip) && isPortValid(port)
    }

    sealed class Label {
        data class StartGame(val ip: String, val port: Int) : Label()
        data object NavigateBack : Label()
    }
}
