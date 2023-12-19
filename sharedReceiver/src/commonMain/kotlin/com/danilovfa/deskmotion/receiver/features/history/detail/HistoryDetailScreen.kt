package com.danilovfa.deskmotion.receiver.features.history.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aay.compose.radarChart.RadarChart
import com.aay.compose.radarChart.model.NetLinesStyle
import com.aay.compose.radarChart.model.Polygon
import com.aay.compose.radarChart.model.PolygonStyle
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.history.detail.store.HistoryDetailStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.buttons.Button
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import com.danilovfa.deskmotion.utils.time.formattedDateTime
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HistoryDetailScreen(component: HistoryDetailComponent) {
    val state by component.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.details),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { component.onEvent(HistoryDetailStore.Intent.OnBackClicked) }
        )

        HistoryDetailLayout(component, state)
    }
}

@Composable
private fun HistoryDetailLayout(
    component: HistoryDetailComponent,
    state: HistoryDetailStore.State
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(DeskMotionDimension.layoutLargeMargin),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.isLoading.not()) {
            HistoryDetails(component, state)

            if (state.radioChartData.isNotEmpty()) {
                HSpacer(DeskMotionDimension.layoutExtraLargeMargin)
                HistoryChart(state)
            }
        }

        LoaderLayout()

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(HistoryDetailStore.Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(HistoryDetailStore.Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
private fun HistoryDetails(
    component: HistoryDetailComponent,
    state: HistoryDetailStore.State
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp),
        verticalArrangement = Arrangement.Center
    ) {
        state.level?.let { level ->
            DetailsItem(
                text = level.name,
                style = DeskMotionTypography.textMedium24
            )
            VSpacer(DeskMotionDimension.layoutLargeMargin)
        }

        state.playLog?.let { playLog ->
            DetailsItem(formattedDateTime(playLog.completedEpochMillis))
            VSpacer(DeskMotionDimension.layoutMainMargin)
            DetailsItem(stringResource(MR.strings.total_score, playLog.score.toString()))
        }

        VSpacer(DeskMotionDimension.layoutLargeMargin)

        Button(
            text = stringResource(MR.strings.show_path_details),
            onClick = { component.onEvent(HistoryDetailStore.Intent.OnPathDetailsClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DetailsItem(
    text: String,
    style: TextStyle = DeskMotionTypography.textBook24,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        color = DeskMotionTheme.colors.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp),
    )
}

@Composable
private fun HistoryChart(
    state: HistoryDetailStore.State,
    modifier: Modifier = Modifier
) {
    val labels = state.radioChartData.map {
        "${it.angleRange.first}°"
    }

    RadarChart(
        radarLabels = labels,
        labelsStyle = DeskMotionTypography.textBook18.copy(color = DeskMotionTheme.colors.onBackground),
        netLinesStyle = NetLinesStyle(
            netLineColor = DeskMotionTheme.colors.secondary,
            netLinesStrokeWidth = 2f,
            netLinesStrokeCap = StrokeCap.Butt
        ),
        scalarSteps = 5,
        scalarValue = 1.0,
        scalarValuesStyle = DeskMotionTypography.captionMedium12.copy(color = DeskMotionTheme.colors.onBackground),
        polygons = listOf(
            Polygon(
                values = state.radioChartData.map { it.frequency },
                unit = "",
                style = PolygonStyle(
                    fillColor = DeskMotionTheme.colors.primary,
                    fillColorAlpha = 0.4f,
                    borderColor = DeskMotionTheme.colors.primary,
                    borderColorAlpha = 0.8f,
                    borderStrokeWidth = 2f,
                    borderStrokeCap = StrokeCap.Butt
                )
            )
        ),
        modifier = modifier
            .size(600.dp)
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(32.dp)
    )
}