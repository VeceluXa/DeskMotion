package com.danilovfa.deskmotion.sender.features.game.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.model.TransferEvent

interface GameStore : Store<GameStore.Intent, GameStore.State, GameStore.Label> {
    sealed class Intent {
        data object OnBackButtonClicked : Intent()
        data object DismissErrorDialog : Intent()
        data object DismissCloseDialog : Intent()
        data object ConfirmCloseDialog : Intent()
    }

    data class State(
        val logs: List<TransferEvent> = emptyList(),
        val errorMessage: String = "",
        val isErrorDialogOpen: Boolean = false,
        val isCloseDialogOpen: Boolean = false,
    )

    sealed class Label {
        data object NavigateBack : Label()
    }
}