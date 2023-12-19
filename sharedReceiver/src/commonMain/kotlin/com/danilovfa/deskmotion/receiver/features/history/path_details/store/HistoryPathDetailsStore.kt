package com.danilovfa.deskmotion.receiver.features.history.path_details.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate

interface HistoryPathDetailsStore :
    Store<HistoryPathDetailsStore.Intent, HistoryPathDetailsStore.State, HistoryPathDetailsStore.Label> {
    sealed class Intent {
        data object OnBackClicked : Intent()
        data object DismissErrorDialog : Intent()
    }

    data class State(
        val logs: List<Coordinate> = emptyList(),
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = ""
    )

    sealed class Label {
        data object NavigateBack : Label()
    }
}