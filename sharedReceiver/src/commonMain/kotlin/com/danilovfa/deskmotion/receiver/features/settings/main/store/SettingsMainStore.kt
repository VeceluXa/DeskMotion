package com.danilovfa.deskmotion.receiver.features.settings.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.utils.locale.DeskMotionLocale
import com.danilovfa.deskmotion.utils.validators.isIpValid
import com.danilovfa.deskmotion.utils.validators.isPortValid

interface SettingsMainStore : Store<SettingsMainStore.Intent, SettingsMainStore.State, SettingsMainStore.Label> {

    sealed class Intent {
        data class OnLanguageSelected(val locale: DeskMotionLocale) : Intent()
        data class OnIpChanged(val ip: String) : Intent()
        data class OnPortChanged(val port: String) : Intent()
        data object OnUserConfigClicked : Intent()
        data object OnSaveClicked : Intent()
    }

    data class State(
        val locale: DeskMotionLocale = DeskMotionLocale.ENGLISH,
        val ip: String = "",
        val port: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) {
        val isSaveButtonEnabled get() = isIpValid(ip) && isPortValid(port) && !isError && !isLoading
    }

    sealed class Label {
        data object Restart : Label()
        data object OnUserConfigClicked : Label()
    }
}