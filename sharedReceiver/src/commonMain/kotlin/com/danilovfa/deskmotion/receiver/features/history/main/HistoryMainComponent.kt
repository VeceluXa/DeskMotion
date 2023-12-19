package com.danilovfa.deskmotion.receiver.features.history.main

import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore
import kotlinx.coroutines.flow.StateFlow

interface HistoryMainComponent {

    val state: StateFlow<HistoryMainStore.State>

    fun onEvent(event: HistoryMainStore.Intent)

    sealed class Output {
        data class OnPlayLogClicked(val logId: Long) : Output()
    }
}