package com.danilovfa.deskmotion.receiver.features.game.select_connection

import com.arkivanov.decompose.ComponentContext
import com.danilovfa.deskmotion.receiver.domain.model.Level

class DefaultSelectConnectionComponent(
    private val componentContext: ComponentContext,
    private val level: Level,
    private val output: (output: GameSelectConnectionComponent.Output) -> Unit
) : GameSelectConnectionComponent, ComponentContext by componentContext {
    override fun onWifiConfigClicked() = output(GameSelectConnectionComponent.Output.OnWifiClicked(level))
    override fun onBluetoothConfigClicked() = output(GameSelectConnectionComponent.Output.OnBluetoothClicked)
    override fun onBackClicked() = output(GameSelectConnectionComponent.Output.NavigateBack)
}