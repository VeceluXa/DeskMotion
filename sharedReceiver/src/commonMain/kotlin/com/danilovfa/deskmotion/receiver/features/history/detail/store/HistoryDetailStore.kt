package com.danilovfa.deskmotion.receiver.features.history.detail.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart.PolarFrequency

interface HistoryDetailStore : Store<HistoryDetailStore.Intent, HistoryDetailStore.State, HistoryDetailStore.Label> {
    sealed class Intent {
        data object OnBackClicked : Intent()
        data object OnPathDetailsClicked : Intent()
        data object DismissErrorDialog : Intent()
    }

    data class State(
        val playLog: PlayLog? = null,
        val level: Level? = null,
        val isLevelLoading: Boolean = false,
        val isLogLoading: Boolean = false,
        val isError: Boolean = false,
        val errorMessage: String = "",
        val radioChartData: List<PolarFrequency> = emptyList()
    ) {
        val isLoading get() = isLevelLoading || isLogLoading
    }

    sealed class Label {
        data object NavigateBack : Label()
        data class NavigateToHeatMap(val logId: Long) : Label()
    }
}