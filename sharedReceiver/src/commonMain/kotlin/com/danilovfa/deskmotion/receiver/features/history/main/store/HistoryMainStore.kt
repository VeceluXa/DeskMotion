package com.danilovfa.deskmotion.receiver.features.history.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog

interface HistoryMainStore: Store<HistoryMainStore.Intent, HistoryMainStore.State, HistoryMainStore.Label> {
    sealed class Intent {
        data class OnPlayLogClicked(val log: PlayLog) : Intent()
        data object DismissErrorDialog : Intent()
    }

    data class State(
        val logs: List<PlayLog> = emptyList(),
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) {
        val isEmpty get() = logs.isEmpty() && !isLoading && !isError
    }

    sealed class Label {
        data class OnPlayLogClicked(val log: PlayLog) : Label()
    }
}