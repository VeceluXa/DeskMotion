package com.danilovfa.deskmotion.receiver.features.history.path_details

import com.danilovfa.deskmotion.receiver.features.history.path_details.store.HistoryPathDetailsStore
import kotlinx.coroutines.flow.StateFlow

interface HistoryPathDetailsComponent {

    val state: StateFlow<HistoryPathDetailsStore.State>

    fun onEvent(event: HistoryPathDetailsStore.Intent)

    sealed class Output {
        data object NavigateBack : Output()
    }
}