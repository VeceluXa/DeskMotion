package com.danilovfa.deskmotion.receiver.features.settings.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.receiver.features.common.user_config.DefaultUserConfigComponent
import com.danilovfa.deskmotion.receiver.features.common.user_config.UserConfigComponent
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
            DefaultSettingsMainComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                output = ::onMainOutput
            )
        )

        Config.UserConfig -> SettingsRootComponent.Child.UserConfig(
            DefaultUserConfigComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                isSettings = true,
                output = ::onUserConfigOutput
            )
        )
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onMainOutput(output: SettingsMainComponent.Output) = when (output) {
        SettingsMainComponent.Output.Restart -> output(SettingsRootComponent.Output.Restart)
        SettingsMainComponent.Output.OpenUserConfig -> {
            navigation.pushNew(Config.UserConfig)
        }
    }

    private fun onUserConfigOutput(output: UserConfigComponent.Output) = when (output) {
        UserConfigComponent.Output.NavigateBack -> {
            navigation.pop()
        }

        UserConfigComponent.Output.NavigateNext -> {
            navigation.pop()
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data object UserConfig : Config
    }
}