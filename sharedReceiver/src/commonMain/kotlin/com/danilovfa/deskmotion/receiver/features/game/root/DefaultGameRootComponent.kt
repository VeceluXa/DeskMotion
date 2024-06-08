package com.danilovfa.deskmotion.receiver.features.game.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.library.connection.ConnectionData
import com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth.BluetoothComponent
import com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth.DefaultBluetoothComponent
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.DefaultWifiComponent
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.WifiComponent
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.common.user_config.DefaultUserConfigComponent
import com.danilovfa.deskmotion.receiver.features.common.user_config.UserConfigComponent
import com.danilovfa.deskmotion.receiver.features.game.finish.DefaultGameFinishComponent
import com.danilovfa.deskmotion.receiver.features.game.finish.GameFinishComponent
import com.danilovfa.deskmotion.receiver.features.game.game.DefaultGameComponent
import com.danilovfa.deskmotion.receiver.features.game.game.GameComponent
import com.danilovfa.deskmotion.receiver.features.game.levels.DefaultLevelsComponent
import com.danilovfa.deskmotion.receiver.features.game.levels.LevelsComponent
import com.danilovfa.deskmotion.receiver.features.game.select_connection.DefaultSelectConnectionComponent
import com.danilovfa.deskmotion.receiver.features.game.select_connection.GameSelectConnectionComponent
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_IP_KEY
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_PORT_KEY
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_USER_ID
import com.danilovfa.deskmotion.utils.validators.isIpValid
import com.danilovfa.deskmotion.utils.validators.isPortValid
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import kotlinx.serialization.Serializable

class DefaultGameRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : GameRootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val settings = Settings()

    private val stack =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = {
                if (settings.getLongOrNull(SETTINGS_USER_ID) != null) {
                    listOf(Config.Levels)
                } else {
                    listOf(Config.UserConfig)
                }
            },
            childFactory = ::child
        )

    override val childrenStack = stack

    private fun child(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Bluetooth -> GameRootComponent.Child.Bluetooth(
            DefaultBluetoothComponent(
                componentContext = componentContext,
                output = ::onBluetoothOutput
            )
        )

        is Config.Game -> GameRootComponent.Child.Game(
            DefaultGameComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                level = config.level,
                connectionData = config.connectionData,
                output = ::onGameOutput
            )
        )

        is Config.SelectConnection -> GameRootComponent.Child.Main(
            DefaultSelectConnectionComponent(
                componentContext = componentContext,
                level = config.level,
                output = ::onSelectConnectionOutput
            )
        )

        is Config.Wifi -> GameRootComponent.Child.Wifi(
            DefaultWifiComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                level = config.level,
                output = ::onWifiOutput
            )
        )

        Config.Levels -> GameRootComponent.Child.Levels(
            DefaultLevelsComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                output = ::onLevelsOutput
            )
        )

        is Config.FinishScreen -> GameRootComponent.Child.Finish(
            DefaultGameFinishComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                playLog = config.log,
                output = ::onGameFinishOutput
            )
        )

        is Config.UserConfig -> GameRootComponent.Child.User(
            DefaultUserConfigComponent(
                storeFactory = storeFactory,
                componentContext = componentContext,
                isSettings = false,
                output = ::onUserOutput
            )
        )
    }

    private fun onGameFinishOutput(output: GameFinishComponent.Output) {
        when (output) {
            GameFinishComponent.Output.NavigateBack -> navigation.popWhile { config ->
                config != Config.Levels
            }
        }
    }

    private fun onUserOutput(output: UserConfigComponent.Output) = when (output) {
        UserConfigComponent.Output.NavigateBack -> throw NotImplementedError("Does not navigate back")
        is UserConfigComponent.Output.NavigateNext -> {
            navigation.replaceAll(Config.Levels)
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onLevelsOutput(output: LevelsComponent.Output) {
        when (output) {
            is LevelsComponent.Output.LevelSelected -> {
                val ip = settings[SETTINGS_IP_KEY] ?: ""
                val port = settings[SETTINGS_PORT_KEY] ?: ""

                if (isIpValid(ip) && isPortValid(port)) {
                    navigation.pushNew(
                        Config.Game(
                            level = output.level,
                            connectionData = ConnectionData.Wifi(
                                ip = ip,
                                port = port.toInt()
                            )
                        )
                    )
                } else {
                    navigation.pushNew(Config.SelectConnection(output.level))
                }
            }
        }
    }

    private fun onBluetoothOutput(output: BluetoothComponent.Output) {
        when (output) {
            BluetoothComponent.Output.NavigateBack -> navigation.pop()
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onWifiOutput(output: WifiComponent.Output) {
        when (output) {
            WifiComponent.Output.NavigateBack -> navigation.pop()
            is WifiComponent.Output.NavigateToGame -> navigation.pushNew(
                Config.Game(output.level, ConnectionData.Wifi(output.ip, output.port))
            )
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onSelectConnectionOutput(output: GameSelectConnectionComponent.Output) {
        when (output) {
            GameSelectConnectionComponent.Output.OnBluetoothClicked -> navigation.pushNew(Config.Bluetooth)
            is GameSelectConnectionComponent.Output.OnWifiClicked -> navigation.pushNew(
                Config.Wifi(
                    output.level
                )
            )

            GameSelectConnectionComponent.Output.NavigateBack -> navigation.pop()
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onGameOutput(output: GameComponent.Output) {
        when (output) {
            GameComponent.Output.NavigateBack -> navigation.pop()
            is GameComponent.Output.EndGame -> navigation.pushNew(Config.FinishScreen(output.log))
        }
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object UserConfig : Config

        @Serializable
        data object Bluetooth : Config

        @Serializable
        data class Wifi(val level: Level) : Config

        @Serializable
        data class SelectConnection(val level: Level) : Config

        @Serializable
        data class Game(val level: Level, val connectionData: ConnectionData) : Config

        @Serializable
        data object Levels : Config

        @Serializable
        data class FinishScreen(val log: PlayLog) : Config
    }
}