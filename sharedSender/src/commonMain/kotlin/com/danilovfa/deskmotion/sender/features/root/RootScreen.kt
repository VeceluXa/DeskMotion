package com.danilovfa.deskmotion.sender.features.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.sender.features.configuration.bluetooth.BluetoothScreen
import com.danilovfa.deskmotion.sender.features.configuration.wifi.WifiScreen
import com.danilovfa.deskmotion.sender.features.game.GameScreen
import com.danilovfa.deskmotion.sender.features.main.MainScreen

@Composable
fun RootScreen(component: RootComponent) {
    val childrenStack by component.childrenStack.subscribeAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Children(childrenStack) {
                when (val child = it.instance) {
                    is RootComponent.Child.Main -> MainScreen(child.component)
                    is RootComponent.Child.Bluetooth -> BluetoothScreen(child.component)
                    is RootComponent.Child.Wifi -> WifiScreen(child.component)
                    is RootComponent.Child.Game -> GameScreen(child.component)
                }
            }
        }
    }
}