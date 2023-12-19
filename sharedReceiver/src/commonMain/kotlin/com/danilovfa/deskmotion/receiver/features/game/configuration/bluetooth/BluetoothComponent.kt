package com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth

interface BluetoothComponent {
    fun onBackClicked()

    sealed class Output {
        data object NavigateBack : Output()
    }
}