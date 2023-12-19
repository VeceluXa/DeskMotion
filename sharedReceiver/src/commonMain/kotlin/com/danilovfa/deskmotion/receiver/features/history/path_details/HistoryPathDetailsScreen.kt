package com.danilovfa.deskmotion.receiver.features.history.path_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.features.history.path_details.store.HistoryPathDetailsStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HistoryPathDetailsScreen(component: HistoryPathDetailsComponent) {
    val state by component.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.path_details),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { component.onEvent(HistoryPathDetailsStore.Intent.OnBackClicked) }
        )
        HistoryPathDetailsLayout(component, state)
    }
}

@Composable
private fun HistoryPathDetailsLayout(
    component: HistoryPathDetailsComponent,
    state: HistoryPathDetailsStore.State
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (state.logs.isNotEmpty()) {
            PathCanvas(state.logs)
        }

        LoaderLayout(showLoader = state.isLoading)

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(HistoryPathDetailsStore.Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(HistoryPathDetailsStore.Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
private fun PathCanvas(logs: List<Coordinate>) {
    val hitColor = DeskMotionTheme.colors.primary

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val offsets = with(LocalDensity.current) {
            logs.map {
                val transformedCoordinate = it.toGameScreenCoordinate(maxWidth.roundToPx(), maxHeight.roundToPx())
                Offset(transformedCoordinate.x.toFloat(), transformedCoordinate.y.toFloat())
            }
        }

        Canvas(Modifier.fillMaxSize()) {
            offsets.forEach { offset ->
                drawCircle(
                    color = hitColor,
                    radius = 5f,
                    center = offset
                )
            }
        }
    }
}