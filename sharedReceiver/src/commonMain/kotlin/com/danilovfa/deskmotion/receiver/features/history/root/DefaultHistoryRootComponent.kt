package com.danilovfa.deskmotion.receiver.features.history.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.history.detail.DefaultHistoryDetailComponent
import com.danilovfa.deskmotion.receiver.features.history.detail.HistoryDetailComponent
import com.danilovfa.deskmotion.receiver.features.history.main.DefaultHistoryMainComponent
import com.danilovfa.deskmotion.receiver.features.history.main.HistoryMainComponent
import com.danilovfa.deskmotion.receiver.features.history.path_details.DefaultHistoryPathDetailsComponent
import com.danilovfa.deskmotion.receiver.features.history.path_details.HistoryPathDetailsComponent
import kotlinx.serialization.Serializable

class DefaultHistoryRootComponent(
    private val componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : HistoryRootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.Main) },
        childFactory = ::child
    )

    override val childrenStack = stack

    private fun child(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Main -> HistoryRootComponent.Child.Main(
            DefaultHistoryMainComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                output = ::onMainOutput
            )
        )

        is Config.Details -> HistoryRootComponent.Child.Detail(
            DefaultHistoryDetailComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                playLogId = config.playLogId,
                output = ::onDetailOutput
            )
        )

        is Config.PathDetails -> HistoryRootComponent.Child.PathDetails(
            DefaultHistoryPathDetailsComponent(
                componentContext = componentContext,
                storeFactory = storeFactory,
                logId = config.playLogId,
                output = ::onPathDetailsOutput
            )
        )
    }

    private fun onPathDetailsOutput(output: HistoryPathDetailsComponent.Output) {
        when (output) {
            HistoryPathDetailsComponent.Output.NavigateBack -> navigation.pop()
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onDetailOutput(output: HistoryDetailComponent.Output) {
        when (output) {
            HistoryDetailComponent.Output.NavigateBack -> navigation.pop()
            is HistoryDetailComponent.Output.NavigateToHeatMapDetails -> navigation.pushNew(Config.PathDetails(output.playLogId))
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun onMainOutput(output: HistoryMainComponent.Output) {
        when (output) {
            is HistoryMainComponent.Output.OnPlayLogClicked -> navigation.pushNew(Config.Details(output.logId))
        }
    }

    @Serializable
    sealed interface Config {

        @Serializable
        data object Main : Config

        @Serializable
        data class Details(val playLogId: Long) : Config

        @Serializable
        data class PathDetails(val playLogId: Long) : Config
    }
}