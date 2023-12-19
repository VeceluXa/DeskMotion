package com.danilovfa.deskmotion.receiver.features.game.game

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.library.connection.ConnectionData
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.features.game.game.store.GameStore
import com.danilovfa.deskmotion.receiver.features.game.game.store.GameStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultGameComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val level: Level,
    private val connectionData: ConnectionData,
    private val output: (GameComponent.Output) -> Unit
) : GameComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val backCallback = BackCallback {
        onEvent(GameStore.Intent.OnBackClicked)
    }

    init {
        backHandler.register(backCallback)
        backCallback.isEnabled = true
    }

    private val gameStore = instanceKeeper.getStore {
        GameStoreFactory(storeFactory, level, connectionData).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = gameStore.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        gameStore.labels
            .onEach { label ->
                when (label) {
                    GameStore.Label.NavigateBack -> output(GameComponent.Output.NavigateBack)
                    is GameStore.Label.EndGame -> output(GameComponent.Output.EndGame(label.playLog))
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: GameStore.Intent) = gameStore.accept(event)
}