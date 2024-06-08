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
import com.danilovfa.deskmotion.receiver.features.settings.root.DefaultSettingsRootComponent
import com.danilovfa.deskmotion.receiver.features.settings.root.SettingsRootComponent
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_LANGUAGE_KEY
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import com.danilovfa.deskmotion.receiver.utils.locale.DeskMotionLocale
import com.danilovfa.deskmotion.receiver.utils.locale.setLocale
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent

class DefaultRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (RootComponent.Output) -> Unit
) : RootComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val settings = Settings()

    init {
        val locale = when(settings.getString(SETTINGS_LANGUAGE_KEY, DeskMotionLocale.ENGLISH.code)) {
            DeskMotionLocale.ENGLISH.code -> DeskMotionLocale.ENGLISH
            else -> DeskMotionLocale.RUSSIAN
        }
        setLocale(locale)

//        preferencesDataStore.data
//            .onEach { preferences ->
//                if (preferences.locale.isCurrent().not()) {
//                    setLocale(preferences.locale)
//                }
//            }
//            .launchIn(scope)
    }

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

        Config.Settings -> RootComponent.Child.Settings(
            DefaultSettingsRootComponent(
                componentContext,
                storeFactory,
                ::onSettingsOutput
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

    private fun onSettingsOutput(output: SettingsRootComponent.Output) {
        when (output) {
            SettingsRootComponent.Output.Restart -> output(RootComponent.Output.Restart)
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Game : Config

        @Serializable
        data object History : Config

        @Serializable
        data object Settings : Config
    }
}