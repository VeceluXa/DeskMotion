package com.danilovfa.deskmotion.receiver.features.game.configuration.user.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.Intent
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.Label
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.State
import kotlinx.datetime.LocalDate

interface UserConfigStore : Store<Intent, State, Label> {
    sealed class Intent {
        data object OnBackClicked : Intent()
        data class OnFirstNameChanged(val firstName: String) : Intent()
        data class OnLastNameChanged(val lastName: String) : Intent()
        data class OnMiddleNameChanged(val middleName: String) : Intent()
        data object OnNextClicked : Intent()
        data object OnErrorDismissed : Intent()
        data class OnDatePickerConfirmed(val dateOfBirth: LocalDate) : Intent()
        data object OnDatePickerDismissed : Intent()
        data object OnDateOfBirthClicked : Intent()
    }

    data class State(
        val firstName: String = "",
        val lastName: String = "",
        val middleName: String = "",
        val dateOfBirth: LocalDate? = null,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val isDatePickerVisible: Boolean = false
    ) {
        val isNextButtonEnabled: Boolean get() = firstName != "" && lastName != "" && !isError
    }

    sealed class Label {
        data object NavigateBack : Label()
        data class NavigateNext(val firstName: String, val lastName: String, val middleName: String) : Label()
    }
}