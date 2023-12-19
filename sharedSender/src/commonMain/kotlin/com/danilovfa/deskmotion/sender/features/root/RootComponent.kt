package com.danilovfa.deskmotion.sender.features.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.sender.features.configuration.bluetooth.BluetoothComponent
import com.danilovfa.deskmotion.sender.features.configuration.wifi.WifiComponent
import com.danilovfa.deskmotion.sender.features.game.GameComponent
import com.danilovfa.deskmotion.sender.features.main.MainComponent

interface RootComponent {
    val childrenStack: Value<ChildStack<*, Child>>

    sealed class Child{
        data class Main(val component: MainComponent) : Child()
        data class Wifi(val component: WifiComponent) : Child()
        data class Bluetooth(val component: BluetoothComponent) : Child()
        data class Game(val component: GameComponent) : Child()
    }
}