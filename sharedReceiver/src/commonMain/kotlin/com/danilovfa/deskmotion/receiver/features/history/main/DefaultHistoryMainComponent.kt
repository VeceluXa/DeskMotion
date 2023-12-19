package com.danilovfa.deskmotion.receiver.features.history.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultHistoryMainComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (HistoryMainComponent.Output) -> Unit
) : HistoryMainComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val historyMainStore = instanceKeeper.getStore {
        HistoryMainStoreFactory(storeFactory).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = historyMainStore.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        historyMainStore.labels
            .onEach { label ->
                when (label) {
                    is HistoryMainStore.Label.OnPlayLogClicked -> {
                        label.log.id?.let { logId ->
                            output(HistoryMainComponent.Output.OnPlayLogClicked(logId))
                        }
                    }
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: HistoryMainStore.Intent) = historyMainStore.accept(event)
}