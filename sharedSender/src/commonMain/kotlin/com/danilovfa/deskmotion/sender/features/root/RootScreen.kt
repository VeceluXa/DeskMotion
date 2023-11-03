package com.danilovfa.deskmotion.sender.features.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.sender.features.main.MainScreen

@Composable
fun RootScreen(component: RootComponent) {
    val childrenStack by component.childrenStack.subscribeAsState()
    Children(childrenStack) {
        when (val child = it.instance) {
            is RootComponent.Child.MainChild -> MainScreen(component = child.component)
        }
    }
}