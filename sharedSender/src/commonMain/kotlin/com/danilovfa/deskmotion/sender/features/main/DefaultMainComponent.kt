package com.danilovfa.deskmotion.sender.features.main

import com.arkivanov.decompose.ComponentContext

class DefaultMainComponent(
    private val componentContext: ComponentContext,
    private val output: (output: MainComponent.Output) -> Unit,
) : MainComponent, ComponentContext by componentContext {

    override fun onWifiConfigClicked() = onOutput(MainComponent.Output.NavigateToWifiConfig)
    override fun onBluetoothConfigClicked() = onOutput(MainComponent.Output.NavigateToBluetoothConfig)

    private fun onOutput(output: MainComponent.Output) = output(output)


}