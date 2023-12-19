package com.danilovfa.deskmotion.receiver.features.game.game.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.model.TransferEvent
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_HEIGHT
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_WIDTH

interface GameStore: Store<GameStore.Intent, GameStore.State, GameStore.Label> {
    sealed class Intent {
        data object OnBackClicked : Intent()
        data object ConfirmCloseDialog : Intent()
        data object DismissCloseDialog : Intent()
        data object DismissErrorDialog : Intent()
    }

    data class State(
        val lastReceivedEvent: TransferEvent? = null,
        val score: Int = 0,
        val targets: List<Coordinate> = emptyList(),
        val log: List<Coordinate> = emptyList(),
        val cursor: Coordinate = Coordinate(
            x = GAME_SCREEN_WIDTH / 2,
            y = GAME_SCREEN_HEIGHT / 2,
            millis = 0
        ),
        val millis: Long = 0L,
        val isCloseDialogOpen: Boolean = false,
        val isErrorDialogOpen: Boolean = false,
        val errorMessage: String = ""
    ) {
        val isLoaderVisible get() = lastReceivedEvent == null
        val currentTarget get() = if (targets.isNotEmpty()) targets.first() else null
    }

    sealed class Label {
        data object NavigateBack : Label()
        data class EndGame(val playLog: PlayLog) : Label()
    }
}