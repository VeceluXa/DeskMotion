package com.danilovfa.deskmotion.receiver.features.common.user_config

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.common.user_config.UserConfigComponent.Output
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultUserConfigComponent(
    private val storeFactory: StoreFactory,
    private val componentContext: ComponentContext,
    private val isSettings: Boolean,
    private val output: (Output) -> Unit
) : UserConfigComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val store = instanceKeeper.getStore {
        UserConfigStoreFactory(storeFactory, isSettings).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val stateFlow = store.stateFlow

    init {
        observeLabel()
    }

    private fun observeLabel() {
        store.labels
            .onEach { label ->
                when (label) {
                    UserConfigStore.Label.NavigateBack -> output(Output.NavigateBack)
                    is UserConfigStore.Label.NavigateNext -> output(Output.NavigateNext)
                }
            }
            .launchIn(scope)
    }

    override fun onIntent(intent: UserConfigStore.Intent) = store.accept(intent)
}