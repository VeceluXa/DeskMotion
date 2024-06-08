package com.danilovfa.deskmotion.receiver.features.settings.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.settings.main.SettingsMainComponent.Output
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultSettingsMainComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (Output) -> Unit
) : SettingsMainComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val store = instanceKeeper.getStore {
        SettingsMainStoreFactory(storeFactory).create()
    }

    init {
        observeLabels()
    }

    private fun observeLabels() {
        store.labels
            .onEach { label ->
                when (label) {
                    SettingsMainStore.Label.Restart -> output(Output.Restart)
                    SettingsMainStore.Label.OnUserConfigClicked -> output(Output.OpenUserConfig)
                }
            }
            .launchIn(scope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun onEvent(event: SettingsMainStore.Intent) = store.accept(event)
}