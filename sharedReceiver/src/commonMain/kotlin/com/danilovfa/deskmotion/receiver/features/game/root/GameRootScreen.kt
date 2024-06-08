package com.danilovfa.deskmotion.receiver.features.game.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth.BluetoothScreen
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.UserConfigScreen
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.WifiScreen
import com.danilovfa.deskmotion.receiver.features.game.finish.GameFinishScreen
import com.danilovfa.deskmotion.receiver.features.game.game.GameScreen
import com.danilovfa.deskmotion.receiver.features.game.levels.LevelsScreen
import com.danilovfa.deskmotion.receiver.features.game.select_connection.GameMainScreen

@Composable
fun GameRootScreen(component: GameRootComponent) {
    val childrenStack by component.childrenStack.subscribeAsState()
    Children(childrenStack) {
        when (val child = it.instance) {
            is GameRootComponent.Child.Bluetooth -> BluetoothScreen(child.component)
            is GameRootComponent.Child.Game -> GameScreen(child.component)
            is GameRootComponent.Child.Main -> GameMainScreen(child.component)
            is GameRootComponent.Child.Wifi -> WifiScreen(child.component)
            is GameRootComponent.Child.Levels -> LevelsScreen(child.component)
            is GameRootComponent.Child.Finish -> GameFinishScreen(child.component)
            is GameRootComponent.Child.User -> UserConfigScreen(child.component)
        }
    }
}