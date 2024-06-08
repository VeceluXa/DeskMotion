package com.danilovfa.deskmotion.receiver.features.settings.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.receiver.features.settings.main.DefaultSettingsMainComponent
import com.danilovfa.deskmotion.receiver.features.settings.main.SettingsMainComponent
import kotlinx.serialization.Serializable

class DefaultSettingsRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (SettingsRootComponent.Output) -> Unit
) : SettingsRootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Main) },
        childFactory = ::child
    )

    override val childrenStack = stack

    private fun child(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Main -> SettingsRootComponent.Child.Main(
            DefaultSettingsMainComponent(componentContext, storeFactory, ::onMainOutput)
        )
    }

    private fun onMainOutput(output: SettingsMainComponent.Output) {
        when (output) {
            SettingsMainComponent.Output.Restart -> output(SettingsRootComponent.Output.Restart)
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config
    }
}