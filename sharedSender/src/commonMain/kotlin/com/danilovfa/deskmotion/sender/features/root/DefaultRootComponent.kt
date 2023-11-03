package com.danilovfa.deskmotion.sender.features.root

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.SimpleNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.sender.features.main.DefaultMainComponent
import com.danilovfa.deskmotion.sender.features.main.MainComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class DefaultRootComponent(
    componentContext: ComponentContext
): RootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()

    private val stack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = { listOf(Config.Main) },
            childFactory = ::child
        )

    override val childrenStack: Value<ChildStack<*, RootComponent.Child>> = stack

    private fun child(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Main -> RootComponent.Child.MainChild(DefaultMainComponent(componentContext))
    }


    @Serializable
    sealed interface Config {
        data object Main : Config
    }
}