package com.danilovfa.deskmotion.sender.features.main

import com.arkivanov.decompose.ComponentContext

class DefaultMainComponent(
    componentContext: ComponentContext
) : MainComponent, ComponentContext by componentContext {

}