package com.danilovfa.deskmotion.receiver.features.game.configuration.wifi

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.store.WifiStore
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.store.WifiStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DefaultWifiComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val level: Level,
    private val output: (output: WifiComponent.Output) -> Unit,
) : WifiComponent, ComponentContext by componentContext {

    private val backCallback = BackCallback {
        onOutput(WifiComponent.Output.NavigateBack)
    }

    init {
        backHandler.register(backCallback)
        backCallback.isEnabled = true
    }

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val wifiStore = instanceKeeper.getStore {
        WifiStoreFactory(storeFactory).create()
    }

    init {
        observeLabels()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = wifiStore.stateFlow

    override fun onEvent(event: WifiStore.Intent) {
        wifiStore.accept(event)
    }

    private fun observeLabels() {
        wifiStore.labels
            .onEach { label ->
                when (label) {
                    is WifiStore.Label.StartGame -> onOutput(
                        WifiComponent.Output.NavigateToGame(level, label.ip, label.port)
                    )

                    WifiStore.Label.NavigateBack -> onOutput(WifiComponent.Output.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    private fun onOutput(output: WifiComponent.Output) {
        output(output)
    }


}