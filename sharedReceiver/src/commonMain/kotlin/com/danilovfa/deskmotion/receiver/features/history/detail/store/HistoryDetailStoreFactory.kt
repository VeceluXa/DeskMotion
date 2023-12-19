package com.danilovfa.deskmotion.receiver.features.history.detail.store

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart.PolarFrequency
import com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart.generateRadarChartData
import com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart.toPolarCoordinate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val playLogId: Long
) : KoinComponent {
    private val repository: DeskMotionRepository by inject()

    fun create(): HistoryDetailStore = object : HistoryDetailStore,
        Store<HistoryDetailStore.Intent, HistoryDetailStore.State, HistoryDetailStore.Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = HistoryDetailStore.State(),
            bootstrapper = SimpleBootstrapper(Action.LoadDetails),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Action {
        data object LoadDetails : Action()
    }

    sealed class Msg {
        data class LoadLevel(val level: Level) : Msg()
        data class LoadPlayLog(val playLog: PlayLog) : Msg()
        data object ShowLevelLoading : Msg()
        data object HideLevelLoading : Msg()
        data object ShowLogLoading : Msg()
        data object HideLogLoading : Msg()
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
        data class UpdateChartData(val frequencies: List<PolarFrequency>) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<HistoryDetailStore.Intent, Action, HistoryDetailStore.State, Msg, HistoryDetailStore.Label>(
            Dispatchers.Main
        ) {
        private var loadLevelJob: Job? = null
        private var loadLogJob: Job? = null

        override fun executeAction(action: Action, getState: () -> HistoryDetailStore.State) =
            when (action) {
                Action.LoadDetails -> loadPlayLog(playLogId)
            }

        override fun executeIntent(
            intent: HistoryDetailStore.Intent,
            getState: () -> HistoryDetailStore.State
        ) = when (intent) {
            HistoryDetailStore.Intent.OnBackClicked -> publish(HistoryDetailStore.Label.NavigateBack)
            HistoryDetailStore.Intent.OnPathDetailsClicked -> {
                getState().playLog?.id?.let { playLogId ->
                    publish(HistoryDetailStore.Label.NavigateToHeatMap(playLogId))
                }
                Unit
            }

            HistoryDetailStore.Intent.DismissErrorDialog -> publish(HistoryDetailStore.Label.NavigateBack)
        }

        private fun loadPlayLog(logId: Long) {
            loadLogJob?.cancel()
            loadLogJob = lceFlow {
                emit(repository.getLog(logId))
            }
                .onEachLoading { dispatch(Msg.ShowLogLoading) }
                .onEachContent { playLog ->
                    dispatch(Msg.HideLogLoading)
                    dispatch(Msg.LoadPlayLog(playLog))
                    generateChartData(playLog.log)
                    loadLevel(playLog.levelId)
                }
                .onEachError { error ->
                    dispatch(Msg.HideLogLoading)
                    dispatch(Msg.HideLevelLoading)
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .launchIn(scope)
        }

        private fun loadLevel(id: Long) {
            loadLevelJob?.cancel()
            loadLevelJob = lceFlow {
                emit(repository.getLevel(id))
            }
                .onEachLoading { dispatch(Msg.ShowLevelLoading) }
                .onEachContent { level ->
                    dispatch(Msg.HideLevelLoading)
                    dispatch(Msg.HideLogLoading)
                    dispatch(Msg.LoadLevel(level))
                }
                .onEachError { error ->
                    dispatch(Msg.HideLevelLoading)
                    dispatch(Msg.HideLogLoading)
                    dispatch(Msg.ShowError(error.message ?: ""))
                }
                .launchIn(scope)
        }

        private fun generateChartData(log: List<Coordinate>) {
            val polarCoordinates = log.map { it.toPolarCoordinate() }
            Logger.i { log.joinToString() }
            Logger.i { polarCoordinates.joinToString() }
            Logger.i { polarCoordinates.map { Math.toDegrees(it.angleInRadians) }.joinToString() }
            val polarFrequencies = generateRadarChartData(polarCoordinates)
            dispatch(Msg.UpdateChartData(polarFrequencies))
        }
    }

    private object ReducerImpl : Reducer<HistoryDetailStore.State, Msg> {
        override fun HistoryDetailStore.State.reduce(msg: Msg): HistoryDetailStore.State =
            when (msg) {
                Msg.ShowLevelLoading -> copy(isLevelLoading = true)
                Msg.HideLevelLoading -> copy(isLevelLoading = false)
                Msg.ShowLogLoading -> copy(isLogLoading = true)
                Msg.HideLogLoading -> copy(isLogLoading = false)
                is Msg.LoadLevel -> copy(level = msg.level)
                is Msg.LoadPlayLog -> copy(playLog = msg.playLog)
                is Msg.ShowError -> copy(isError = true, errorMessage = msg.message)
                Msg.HideError -> copy(isError = false, errorMessage = "")
                is Msg.UpdateChartData -> copy(radioChartData = msg.frequencies)
            }
    }

    companion object {
        const val STORE_NAME = "HistoryDetailStore"
    }
}