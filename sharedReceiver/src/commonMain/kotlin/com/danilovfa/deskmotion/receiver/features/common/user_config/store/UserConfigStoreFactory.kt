package com.danilovfa.deskmotion.receiver.features.common.user_config.store

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.receiver.domain.model.user.User
import com.danilovfa.deskmotion.receiver.domain.repository.UserRepository
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.Intent
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.Label
import com.danilovfa.deskmotion.receiver.features.common.user_config.store.UserConfigStore.State
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_USER_ID
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.launchIn
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserConfigStoreFactory(
    private val storeFactory: StoreFactory,
    private val isSettings: Boolean
) {
    fun create(): UserConfigStore = object : UserConfigStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(isBackButtonVisible = isSettings),
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
        CoroutineExecutor<Intent, Action, State, Msg, Label>(), KoinComponent {
        private val settings = Settings()
        private val userRepository: UserRepository by inject()

        override fun executeAction(action: Action, getState: () -> State) = when (action) {
            Action.LoadSettings -> loadUser()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.OnBackClicked -> publish(Label.NavigateBack)
            Intent.OnErrorDismissed -> publish(Label.NavigateBack)
            is Intent.OnFirstNameChanged -> dispatch(Msg.UpdateFirstName(intent.firstName))
            is Intent.OnLastNameChanged -> dispatch(Msg.UpdateLastName(intent.lastName))
            is Intent.OnMiddleNameChanged -> dispatch(Msg.UpdateMiddleName(intent.middleName))
            Intent.OnSaveClicked -> saveSettings(
                firstName = getState().firstName,
                lastName = getState().lastName,
                middleName = getState().middleName,
                dateOfBirth = getState().dateOfBirth
            )

            is Intent.OnDatePickerConfirmed -> {
                dispatch(Msg.HideDatePicker)
                dispatch(Msg.UpdateDateOfBirth(intent.dateOfBirth))
            }
            Intent.OnDatePickerDismissed -> dispatch(Msg.HideDatePicker)
            Intent.OnDateOfBirthClicked -> dispatch(Msg.ShowDatePicker)
        }

        private fun saveSettings(firstName: String, lastName: String, middleName: String, dateOfBirth: LocalDate) {
            lceFlow {
                val userId = settings.getLongOrNull(SETTINGS_USER_ID)

                Logger.d { "UserId: $userId" }

                val user = User(
                    id = userId ?: 0L,
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName,
                    dateOfBirth = dateOfBirth
                )

                if (userId != null) {
                    userRepository.updateUser(user)
                } else {
                    userRepository.addUser(user)
                }

                emit(Unit)
            }
                .onEachError { error ->
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .onEachContent {
                    publish(Label.NavigateNext)
                }
                .launchIn(scope)
        }

        private fun loadUser() {
            lceFlow {
                val userId = settings.getLongOrNull(SETTINGS_USER_ID)

                if (userId != null) {
                    emit(userRepository.getUser(userId))
                }
            }
                .onEachError { error ->
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .onEachContent { user ->
                    user?.let {
                        dispatch(Msg.UpdateFirstName(user.firstName))
                        dispatch(Msg.UpdateLastName(user.lastName))
                        dispatch(Msg.UpdateMiddleName(user.middleName))
                        dispatch(Msg.UpdateDateOfBirth(user.dateOfBirth))
                    }
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