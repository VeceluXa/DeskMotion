package com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.danilovfa.deskmotion.receiver.features.game.configuration.bluetooth.BluetoothComponent

class DefaultBluetoothComponent(
    private val componentContext: ComponentContext,
    private val output: (output: BluetoothComponent.Output) -> Unit,
) : BluetoothComponent, ComponentContext by componentContext {
    private val backCallback = BackCallback {
        onBackClicked()
    }

    init {
        backHandler.register(backCallback)
        backCallback.isEnabled = true
    }

    override fun onBackClicked() = onOutput(BluetoothComponent.Output.NavigateBack)

    private fun onOutput(output: BluetoothComponent.Output) = output(output)
}