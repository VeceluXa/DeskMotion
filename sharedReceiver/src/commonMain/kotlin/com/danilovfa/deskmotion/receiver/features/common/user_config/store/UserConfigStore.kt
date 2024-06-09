package com.danilovfa.deskmotion.receiver.features.common.user_config.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.Intent
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.Label
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.State
import com.danilovfa.deskmotion.utils.time.now
import kotlinx.datetime.LocalDate

interface UserConfigStore : Store<Intent, State, Label> {
    sealed class Intent {
        data object OnBackClicked : Intent()
        data class OnFirstNameChanged(val firstName: String) : Intent()
        data class OnLastNameChanged(val lastName: String) : Intent()
        data class OnMiddleNameChanged(val middleName: String) : Intent()
        data object OnSaveClicked : Intent()
        data object OnErrorDismissed : Intent()
        data object OnDeleteClicked : Intent()
        data class OnDatePickerConfirmed(val dateOfBirth: LocalDate) : Intent()
        data object OnDatePickerDismissed : Intent()
        data object OnDateOfBirthClicked : Intent()
    }

    data class State(
        val firstName: String = "",
        val lastName: String = "",
        val middleName: String = "",
        val dateOfBirth: LocalDate = LocalDate.now(),
        val isError: Boolean = false,
        val errorMessage: String = "",
        val isDatePickerVisible: Boolean = false,
        val isSettings: Boolean
    ) {
        val isSaveButtonEnabled: Boolean get() = firstName != "" && lastName != "" && !isError
        val isBackButtonVisible: Boolean get() = isSettings
        val isDeleteButtonVisible: Boolean get() = isSettings
    }

    sealed class Label {
        data object NavigateBack : Label()
        data object NavigateNext : Label()
    }
}