package com.danilovfa.deskmotion.receiver.features.settings.main.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore.Intent
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore.Label
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_FIRST_NAME
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_IP_KEY
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_LANGUAGE_KEY
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_LAST_NAME
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_MIDDLE_NAME
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_PORT_KEY
import com.danilovfa.deskmotion.receiver.utils.locale.DeskMotionLocale
import com.danilovfa.deskmotion.receiver.utils.locale.setLocale
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent

class SettingsMainStoreFactory(
    private val storeFactory: StoreFactory
) {
    val settings = Settings()

    fun create(): SettingsMainStore = object : SettingsMainStore,
        Store<Intent, SettingsMainStore.State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = SettingsMainStore.State(),
            bootstrapper = SimpleBootstrapper(Action.GetSettings),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Action {
        data object GetSettings : Action()
    }

    sealed class Msg {
        data class UpdateLocale(val locale: DeskMotionLocale) : Msg()
        data class UpdateIp(val ip: String) : Msg()
        data class UpdatePort(val port: String) : Msg()
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, SettingsMainStore.State, Msg, Label>(
            Dispatchers.Main
        ), KoinComponent {

        override fun executeAction(action: Action, getState: () -> SettingsMainStore.State) =
            when (action) {
                Action.GetSettings -> getPreferences()
            }

        override fun executeIntent(
            intent: Intent,
            getState: () -> SettingsMainStore.State
        ) = when (intent) {
            is Intent.OnLanguageSelected -> updateLocale(intent.locale)
            is Intent.OnIpChanged -> dispatch(Msg.UpdateIp(intent.ip))
            is Intent.OnPortChanged -> dispatch(Msg.UpdatePort(intent.port))
            Intent.OnSaveClicked -> onSaveClicked(
                ip = getState().ip,
                port = getState().port
            )

            Intent.OnUserConfigClicked -> publish(Label.OnUserConfigClicked)
        }

        private fun onSaveClicked(
            ip: String,
            port: String,
        ) {
            lceFlow {
                settings[SETTINGS_IP_KEY] = ip
                settings[SETTINGS_PORT_KEY] = port
                emit(Unit)
            }
                .onEachLoading { dispatch(Msg.ShowLoading) }
                .onEachContent {
                    dispatch(Msg.HideLoading)
                    publish(Label.Restart)
                }
                .onEachError { error ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .launchIn(scope)
        }

        private fun getPreferences() {
            val locale: DeskMotionLocale =
                when (settings.getString(SETTINGS_LANGUAGE_KEY, DeskMotionLocale.ENGLISH.code)) {
                    DeskMotionLocale.ENGLISH.code -> DeskMotionLocale.ENGLISH
                    else -> DeskMotionLocale.RUSSIAN
                }

            val ip: String = settings[SETTINGS_IP_KEY] ?: ""
            val port: String = settings[SETTINGS_PORT_KEY] ?: ""

            dispatch(Msg.UpdateLocale(locale))
            dispatch(Msg.UpdateIp(ip))
            dispatch(Msg.UpdatePort(port))
        }

        private fun updateLocale(locale: DeskMotionLocale) {
            lceFlow {
                settings[SETTINGS_LANGUAGE_KEY] = locale.code
                emit(Unit)
            }
                .onEachLoading { dispatch(Msg.ShowLoading) }
                .onEachContent {
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.UpdateLocale(locale))
                    setLocale(locale)
                    publish(Label.Restart)
                }
                .onEachError { error ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<SettingsMainStore.State, Msg> {
        override fun SettingsMainStore.State.reduce(msg: Msg): SettingsMainStore.State =
            when (msg) {
                is Msg.UpdateLocale -> copy(locale = msg.locale)
                Msg.ShowLoading -> copy(isLoading = true)
                Msg.HideLoading -> copy(isLoading = false)
                is Msg.ShowError -> copy(isError = true, errorMessage = msg.message)
                Msg.HideError -> copy(isError = false, errorMessage = "")
                is Msg.UpdateIp -> copy(ip = msg.ip)
                is Msg.UpdatePort -> copy(port = msg.port)
            }
    }

    companion object {
        const val STORE_NAME = "SettingsMain"
    }
}