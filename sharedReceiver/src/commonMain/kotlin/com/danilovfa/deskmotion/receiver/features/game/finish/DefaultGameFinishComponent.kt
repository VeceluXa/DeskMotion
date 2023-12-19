package com.danilovfa.deskmotion.receiver.features.game.finish

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.game.finish.store.GameFinishStore
import com.danilovfa.deskmotion.receiver.features.game.finish.store.GameFinishStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultGameFinishComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val playLog: PlayLog,
    private val output: (GameFinishComponent.Output) -> Unit
) : GameFinishComponent, ComponentContext by componentContext {
    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val gameFinishStore = instanceKeeper.getStore {
        GameFinishStoreFactory(storeFactory, playLog).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = gameFinishStore.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        gameFinishStore.labels
            .onEach { label ->
                when (label) {
                    GameFinishStore.Label.NavigateBack -> output(GameFinishComponent.Output.NavigateBack)
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: GameFinishStore.Intent) = gameFinishStore.accept(event)
}