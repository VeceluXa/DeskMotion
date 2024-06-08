package com.danilovfa.deskmotion.receiver.features.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.receiver.features.game.root.GameRootComponent
import com.danilovfa.deskmotion.receiver.features.history.main.HistoryMainComponent
import com.danilovfa.deskmotion.receiver.features.history.root.HistoryRootComponent
import com.danilovfa.deskmotion.receiver.features.settings.root.SettingsRootComponent
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val childrenStack: Value<ChildStack<*, Child>>
    val navigationItems: StateFlow<List<NavigationItemData>>

    fun onNavigationItemClick(navigationItem: NavigationItemData)

    sealed class Child {
        data class Game(val component: GameRootComponent) : Child()
        data class History(val component: HistoryRootComponent) : Child()
        data class Settings(val component: SettingsRootComponent) : Child()
    }

    sealed class Output {
        data object Restart : Output()
    }
}