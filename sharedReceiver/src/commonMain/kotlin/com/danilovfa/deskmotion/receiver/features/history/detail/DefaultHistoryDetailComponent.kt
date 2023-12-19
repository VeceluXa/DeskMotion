package com.danilovfa.deskmotion.receiver.features.history.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.history.detail.store.HistoryDetailStore
import com.danilovfa.deskmotion.receiver.features.history.detail.store.HistoryDetailStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultHistoryDetailComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val playLogId: Long,
    private val output: (HistoryDetailComponent.Output) -> Unit
) : HistoryDetailComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val historyDetailStore = instanceKeeper.getStore {
        HistoryDetailStoreFactory(storeFactory, playLogId).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = historyDetailStore.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        historyDetailStore.labels
            .onEach { label ->
                when (label) {
                    HistoryDetailStore.Label.NavigateBack -> output(HistoryDetailComponent.Output.NavigateBack)
                    is HistoryDetailStore.Label.NavigateToHeatMap -> output(HistoryDetailComponent.Output.NavigateToHeatMapDetails(label.logId))
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: HistoryDetailStore.Intent) = historyDetailStore.accept(event)
}