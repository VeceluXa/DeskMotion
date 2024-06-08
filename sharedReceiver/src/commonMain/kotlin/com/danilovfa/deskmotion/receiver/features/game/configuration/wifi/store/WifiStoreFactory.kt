package com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_IP_KEY
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_PORT_KEY
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent

class WifiStoreFactory(private val storeFactory: StoreFactory) : KoinComponent {
    val settings = Settings()

    fun create(): WifiStore = object : WifiStore,
        Store<WifiStore.Intent, WifiStore.State, WifiStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = WifiStore.State(),
            bootstrapper = null,
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed class Msg {
        data class IpChanged(val ip: String) : Msg()
        data class PortChanged(val port: String) : Msg()
    }


    private inner class ExecutorImpl :
        CoroutineExecutor<WifiStore.Intent, Nothing, WifiStore.State, Msg, WifiStore.Label>(Dispatchers.Main) {
        override fun executeIntent(intent: WifiStore.Intent, getState: () -> WifiStore.State) =
            when (intent) {
                is WifiStore.Intent.OnIpChanged -> dispatch(Msg.IpChanged(intent.ip))
                is WifiStore.Intent.OnPortChanged -> dispatch(Msg.PortChanged(intent.port))
                WifiStore.Intent.OnStartClicked -> onStartClicked(getState())
                WifiStore.Intent.OnBackClicked -> publish(WifiStore.Label.NavigateBack)
            }

        fun onStartClicked(state: WifiStore.State) {
            settings[SETTINGS_IP_KEY] = state.ip
            settings[SETTINGS_PORT_KEY] = state.port
            publish(WifiStore.Label.StartGame(state.ip, state.port.toInt()))
        }
    }

    private object ReducerImpl : Reducer<WifiStore.State, Msg> {
        override fun WifiStore.State.reduce(msg: Msg): WifiStore.State = when (msg) {
            is Msg.IpChanged -> copy(ip = msg.ip, isIpError = false)
            is Msg.PortChanged -> copy(port = msg.port, isPortError = false)
        }

    }

    companion object {
        private const val STORE_NAME = "WifiStore"
    }
}