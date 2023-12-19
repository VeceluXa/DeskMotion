package com.danilovfa.deskmotion.library.connection

import kotlinx.serialization.Serializable

@Serializable
sealed class ConnectionData {
    @Serializable
    data class Wifi(val ip: String, val port: Int) : ConnectionData()

    @Serializable
    data object Bluetooth : ConnectionData()
}
