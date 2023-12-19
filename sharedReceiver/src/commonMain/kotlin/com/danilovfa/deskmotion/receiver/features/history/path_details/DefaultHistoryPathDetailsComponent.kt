package com.danilovfa.deskmotion.receiver.features.history.path_details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.history.path_details.store.HistoryPathDetailsStore
import com.danilovfa.deskmotion.receiver.features.history.path_details.store.HistoryPathDetailsStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultHistoryPathDetailsComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val logId: Long,
    private val output: (HistoryPathDetailsComponent.Output) -> Unit
) : HistoryPathDetailsComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val store = instanceKeeper.getStore {
        HistoryPathDetailsStoreFactory(storeFactory, logId).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        store.labels
            .onEach { label ->
                when (label) {
                    HistoryPathDetailsStore.Label.NavigateBack -> output(HistoryPathDetailsComponent.Output.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: HistoryPathDetailsStore.Intent) = store.accept(event)
}