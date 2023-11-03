package com.danilovfa.deskmotion.sender.features.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.sender.features.main.MainComponent

interface RootComponent {
    val childrenStack: Value<ChildStack<*, Child>>

    sealed class Child{
        data class MainChild(val component: MainComponent) : Child()
    }
}