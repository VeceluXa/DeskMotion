package com.danilovfa.deskmotion.sender.features.game

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.library.connection.ConnectionData
import com.danilovfa.deskmotion.sender.features.game.store.GameStore
import com.danilovfa.deskmotion.sender.features.game.store.GameStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultGameComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (output: GameComponent.Output) -> Unit,
    private val connectionData: ConnectionData
) : GameComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val backCallback = BackCallback {
        onEvent(GameStore.Intent.OnBackButtonClicked)
    }

    init {
        backHandler.register(backCallback)
        backCallback.isEnabled = true
    }

    private val gameStore = instanceKeeper.getStore {
        GameStoreFactory(storeFactory, connectionData).create()
    }

    init {
        observeLabels()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = gameStore.stateFlow

    private fun onOutput(output: GameComponent.Output) = output(output)

    override fun onEvent(event: GameStore.Intent) = gameStore.accept(event)

    private fun observeLabels() {
        gameStore.labels
            .onEach { label ->
                when (label) {
                    GameStore.Label.NavigateBack -> onOutput(GameComponent.Output.NavigateBack)
                }
            }
            .launchIn(scope)
    }


}