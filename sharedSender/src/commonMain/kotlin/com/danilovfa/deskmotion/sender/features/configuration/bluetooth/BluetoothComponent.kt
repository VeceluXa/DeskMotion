package com.danilovfa.deskmotion.sender.features.configuration.bluetooth

interface BluetoothComponent {
    fun onBackClicked()

    sealed class Output {
        data object NavigateBack : Output()
    }
}