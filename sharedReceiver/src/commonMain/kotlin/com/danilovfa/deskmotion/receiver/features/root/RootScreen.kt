package com.danilovfa.deskmotion.receiver.features.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.danilovfa.deskmotion.receiver.features.game.root.GameRootScreen
import com.danilovfa.deskmotion.receiver.features.history.root.HistoryRootScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme

@Composable
fun RootScreen(component: RootComponent) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        NavigationLayout(component)
        ChildrenLayout(component, Modifier.weight(1f))
    }
}

@Composable
private fun ChildrenLayout(component: RootComponent, modifier: Modifier = Modifier) {
    val childrenStack by component.childrenStack.subscribeAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            Children(childrenStack) {
                when (val child = it.instance) {
                    is RootComponent.Child.Game -> GameRootScreen(child.component)
                    is RootComponent.Child.History -> HistoryRootScreen(child.component)
                }
            }
        }
    }
}

@Composable
private fun NavigationLayout(component: RootComponent) {
    val navigationItems by component.navigationItems.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                color = DeskMotionTheme.colors.secondaryContainer
            )
    ) {
        navigationItems.forEach { navigationItem ->
            NavigationItem(
                navigationItem = navigationItem,
                onItemClick = {
                    component.onNavigationItemClick(navigationItem)
                }
            )
        }
    }
}

