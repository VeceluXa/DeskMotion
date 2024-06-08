package com.danilovfa.deskmotion.receiver.features.game.configuration.user.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.Intent
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.Label
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.State
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_FIRST_NAME
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_LAST_NAME
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_MIDDLE_NAME
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.launchIn
import kotlinx.datetime.LocalDate

class UserConfigStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): UserConfigStore = object : UserConfigStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.LoadSettings),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Msg {
        data class UpdateFirstName(val firstName: String) : Msg()
        data class UpdateLastName(val lastName: String) : Msg()
        data class UpdateMiddleName(val middleName: String) : Msg()
        data class ShowError(val message: String) : Msg()
        data object ShowDatePicker : Msg()
        data object HideDatePicker : Msg()
        data class UpdateDateOfBirth(val dateOfBirth: LocalDate) : Msg()
    }

    sealed class Action {
        data object LoadSettings : Action()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        private val settings = Settings()

        override fun executeAction(action: Action, getState: () -> State) = when (action) {
            Action.LoadSettings -> loadSettings()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.OnBackClicked -> publish(Label.NavigateBack)
            Intent.OnErrorDismissed -> publish(Label.NavigateBack)
            is Intent.OnFirstNameChanged -> dispatch(Msg.UpdateFirstName(intent.firstName))
            is Intent.OnLastNameChanged -> dispatch(Msg.UpdateLastName(intent.lastName))
            is Intent.OnMiddleNameChanged -> dispatch(Msg.UpdateMiddleName(intent.middleName))
            Intent.OnNextClicked -> saveSettings(
                firstName = getState().firstName,
                lastName = getState().lastName,
                middleName = getState().middleName
            )

            is Intent.OnDatePickerConfirmed -> {
                dispatch(Msg.HideDatePicker)
                dispatch(Msg.UpdateDateOfBirth(intent.dateOfBirth))
            }
            Intent.OnDatePickerDismissed -> dispatch(Msg.HideDatePicker)
            Intent.OnDateOfBirthClicked -> dispatch(Msg.ShowDatePicker)
        }

        private fun saveSettings(firstName: String, lastName: String, middleName: String) {
            lceFlow {
                settings[SETTINGS_FIRST_NAME] = firstName
                settings[SETTINGS_LAST_NAME] = lastName
                settings[SETTINGS_MIDDLE_NAME] = middleName
                emit(Unit)
            }
                .onEachError { error ->
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .onEachContent {
                    publish(Label.NavigateNext(firstName, lastName, middleName))
                }
                .launchIn(scope)
        }

        private fun loadSettings() {
            lceFlow {
                val firstName = settings[SETTINGS_FIRST_NAME] ?: ""
                val lastName = settings[SETTINGS_LAST_NAME] ?: ""
                val middleName = settings[SETTINGS_MIDDLE_NAME] ?: ""

                emit(Name(firstName, lastName, middleName))
            }
                .onEachError { error ->
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .onEachContent { name ->
                    dispatch(Msg.UpdateFirstName(name.firstName))
                    dispatch(Msg.UpdateLastName(name.lastName))
                    dispatch(Msg.UpdateMiddleName(name.middleName))
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ShowError -> copy(isError = true, errorMessage = msg.message)
            is Msg.UpdateFirstName -> copy(firstName = msg.firstName)
            is Msg.UpdateLastName -> copy(lastName = msg.lastName)
            is Msg.UpdateMiddleName -> copy(middleName = msg.middleName)
            Msg.HideDatePicker -> copy(isDatePickerVisible = false)
            Msg.ShowDatePicker -> copy(isDatePickerVisible = true)
            is Msg.UpdateDateOfBirth -> copy(dateOfBirth = msg.dateOfBirth)
        }
    }

    companion object {
        private const val STORE_NAME = "UserConfigStore"
    }
}

private data class Name(
    val firstName: String,
    val lastName: String,
    val middleName: String
)