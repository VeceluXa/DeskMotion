package com.danilovfa.deskmotion.receiver.features.game.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth.BluetoothComponent
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.WifiComponent
import com.danilovfa.deskmotion.receiver.features.game.finish.GameFinishComponent
import com.danilovfa.deskmotion.receiver.features.game.game.GameComponent
import com.danilovfa.deskmotion.receiver.features.game.levels.LevelsComponent
import com.danilovfa.deskmotion.receiver.features.game.select_connection.GameSelectConnectionComponent

interface GameRootComponent {
    val childrenStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Main(val component: GameSelectConnectionComponent) : Child()
        data class Wifi(val component: WifiComponent) : Child()
        data class Bluetooth(val component: BluetoothComponent) : Child()
        data class Game(val component: GameComponent) : Child()
        data class Levels(val component: LevelsComponent) : Child()
        data class Finish(val component: GameFinishComponent) : Child()
    }
}