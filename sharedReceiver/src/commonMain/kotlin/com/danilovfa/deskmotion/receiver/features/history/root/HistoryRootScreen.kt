package com.danilovfa.deskmotion.receiver.features.history.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.receiver.features.history.detail.HistoryDetailScreen
import com.danilovfa.deskmotion.receiver.features.history.main.HistoryMainScreen
import com.danilovfa.deskmotion.receiver.features.history.path_details.HistoryPathDetailsScreen

@Composable
fun HistoryRootScreen(component: HistoryRootComponent) {
    val childrenStack by component.childrenStack.subscribeAsState()
    Children(childrenStack) {
        when (val child = it.instance) {
            is HistoryRootComponent.Child.Main -> HistoryMainScreen(child.component)
            is HistoryRootComponent.Child.Detail -> HistoryDetailScreen(child.component)
            is HistoryRootComponent.Child.PathDetails -> HistoryPathDetailsScreen(child.component)
        }
    }
}