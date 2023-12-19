package com.danilovfa.deskmotion.receiver.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.receiver.features.game.root.DefaultGameRootComponent
import com.danilovfa.deskmotion.receiver.features.history.root.DefaultHistoryRootComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val stack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = { listOf(Config.Game) },
            childFactory = ::child
        )

    override val childrenStack: Value<ChildStack<*, RootComponent.Child>> = stack

    private val _navigationItems = MutableStateFlow(defaultNavigationItems)
    override val navigationItems = _navigationItems.asStateFlow()

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child = when (config) {
        Config.Game -> RootComponent.Child.Game(
            DefaultGameRootComponent(
                componentContext,
                storeFactory
            )
        )
        Config.History -> RootComponent.Child.History(
            DefaultHistoryRootComponent(
                componentContext,
                storeFactory
            )
        )
    }

    override fun onNavigationItemClick(navigationItem: NavigationItemData) {
        val config = navigationItem.config
        val list = _navigationItems.value

        val newList = list.map {
            if (it.config == config) it.copy(isActive = true) else it.copy(isActive = false)
        }

        _navigationItems.update {
            newList
        }

        navigation.bringToFront(config)
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Game : Config

        @Serializable
        data object History : Config
    }
}