package com.danilovfa.deskmotion.sender.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.library.connection.ConnectionData
import com.danilovfa.deskmotion.sender.features.configuration.bluetooth.BluetoothComponent
import com.danilovfa.deskmotion.sender.features.configuration.bluetooth.DefaultBluetoothComponent
import com.danilovfa.deskmotion.sender.features.configuration.wifi.DefaultWifiComponent
import com.danilovfa.deskmotion.sender.features.configuration.wifi.WifiComponent
import com.danilovfa.deskmotion.sender.features.game.DefaultGameComponent
import com.danilovfa.deskmotion.sender.features.game.GameComponent
import com.danilovfa.deskmotion.sender.features.main.DefaultMainComponent
import com.danilovfa.deskmotion.sender.features.main.MainComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : RootComponent, ComponentContext by componentContext, KoinComponent {

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
        Config.Main -> RootComponent.Child.Main(
            DefaultMainComponent(
                componentContext = componentContext,
                output = ::onMainOutput
            )
        )

        Config.Bluetooth -> RootComponent.Child.Bluetooth(
            DefaultBluetoothComponent(
                componentContext = componentContext,
                output = ::onBluetoothOutput
            )
        )

        is Config.Game -> RootComponent.Child.Game(
            DefaultGameComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                connectionData = config.connectionData,
                output = ::onGameOutput
            )
        )

        Config.Wifi -> RootComponent.Child.Wifi(
            DefaultWifiComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                output = ::onWifiOutput
            )
        )
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onMainOutput(output: MainComponent.Output) = when (output) {
        MainComponent.Output.NavigateToBluetoothConfig -> navigation.pushNew(Config.Bluetooth)
        MainComponent.Output.NavigateToWifiConfig -> navigation.pushNew(Config.Wifi)
    }

    private fun onBluetoothOutput(output: BluetoothComponent.Output) = when (output) {
        BluetoothComponent.Output.NavigateBack -> navigation.pop()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onWifiOutput(output: WifiComponent.Output) = when (output) {
        WifiComponent.Output.NavigateBack -> navigation.pop()
        is WifiComponent.Output.NavigateToGame ->
            navigation.pushNew(Config.Game(ConnectionData.Wifi(output.ip, output.port)))
    }

    private fun onGameOutput(output: GameComponent.Output) = when (output) {
        GameComponent.Output.NavigateBack -> navigation.pop()
    }


    @Serializable
    sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data object Wifi : Config

        @Serializable
        data object Bluetooth : Config

        @Serializable
        data class Game(val connectionData: ConnectionData) : Config
    }
}