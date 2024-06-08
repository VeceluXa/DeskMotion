package com.danilovfa.deskmotion.receiver.features.settings.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.receiver.features.settings.main.SettingsMainScreen

@Composable
fun SettingsRootScreen(component: SettingsRootComponent) {
    val children by component.childrenStack.subscribeAsState()
    Column(Modifier.fillMaxSize()) {
        Children(children) {
            when (val child = it.instance) {
                is SettingsRootComponent.Child.Main -> SettingsMainScreen(child.component)
            }
        }
    }
}