package com.danilovfa.deskmotion.receiver.features.game.levels

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStoreFactory
import com.danilovfa.deskmotion.ui.decompose.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultLevelsComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val output: (LevelsComponent.Output) -> Unit
) : LevelsComponent, ComponentContext by componentContext {

    private val scope = coroutineScope(Dispatchers.Main + SupervisorJob())

    private val levelsStore = instanceKeeper.getStore {
        LevelsStoreFactory(storeFactory).create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = levelsStore.stateFlow

    init {
        observeLabels()
    }

    private fun observeLabels() {
        levelsStore.labels
            .onEach { label ->
                when (label) {
                    is LevelsStore.Label.LevelSelected ->
                        output(LevelsComponent.Output.LevelSelected(label.level))
                }
            }
            .launchIn(scope)
    }

    override fun onEvent(event: LevelsStore.Intent) = levelsStore.accept(event)
}