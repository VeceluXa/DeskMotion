package com.danilovfa.deskmotion.receiver.features.history.detail

import com.danilovfa.deskmotion.receiver.features.history.detail.store.HistoryDetailStore
import kotlinx.coroutines.flow.StateFlow

interface HistoryDetailComponent {

    val state: StateFlow<HistoryDetailStore.State>

    fun onEvent(event: HistoryDetailStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
        data class NavigateToHeatMapDetails(val playLogId: Long) : Output()
    }
}