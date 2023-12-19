package com.danilovfa.deskmotion.receiver.features.game.select_connection

import com.danilovfa.deskmotion.receiver.domain.model.Level

interface GameSelectConnectionComponent {

    fun onWifiConfigClicked()
    fun onBluetoothConfigClicked()
    fun onBackClicked()

    sealed class Output {
        data class OnWifiClicked(val level: Level) : Output()
        data object OnBluetoothClicked : Output()
        data object NavigateBack : Output()
    }
}