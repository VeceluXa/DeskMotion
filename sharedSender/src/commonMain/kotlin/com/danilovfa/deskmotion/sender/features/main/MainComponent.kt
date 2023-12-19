package com.danilovfa.deskmotion.sender.features.main

interface MainComponent {
    fun onWifiConfigClicked()
    fun onBluetoothConfigClicked()

    sealed class Output {
        data object NavigateToWifiConfig : Output()
        data object NavigateToBluetoothConfig : Output()
    }
}