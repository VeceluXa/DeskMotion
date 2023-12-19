package com.danilovfa.deskmotion.receiver.features.game.finish.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog


interface GameFinishStore : Store<GameFinishStore.Intent, GameFinishStore.State, GameFinishStore.Label> {
    sealed class Intent {
        data object OnOkClicked : Intent()
        data object DismissErrorDialog : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val playLog: PlayLog
    )

    sealed class Label {
        data object NavigateBack : Label()
    }
}