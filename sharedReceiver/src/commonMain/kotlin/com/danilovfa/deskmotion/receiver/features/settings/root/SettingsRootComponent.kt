package com.danilovfa.deskmotion.receiver.features.settings.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.receiver.features.common.user_config.UserConfigComponent
import com.danilovfa.deskmotion.receiver.features.settings.main.SettingsMainComponent

interface SettingsRootComponent {

    val childrenStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Main(val component: SettingsMainComponent) : Child()
        data class UserConfig(val component: UserConfigComponent) : Child()
    }

    sealed class Output {
        data object Restart : Output()
    }
}