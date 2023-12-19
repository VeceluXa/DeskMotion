package com.danilovfa.deskmotion.receiver.features.game.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.game.game.store.GameStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon

@Composable
fun GameScreen(component: GameComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.app_name),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { component.onEvent(GameStore.Intent.OnBackClicked) }
        )

        if (state.isLoaderVisible.not()) {
            GameLayout(state, Modifier.weight(1f))
        }

        LoaderLayout(showLoader = state.isLoaderVisible)

        AlertDialog(
            isVisible = state.isErrorDialogOpen,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(GameStore.Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(GameStore.Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )

        AlertDialog(
            isVisible = state.isCloseDialogOpen,
            title = stringResource(MR.strings.warning),
            text = stringResource(MR.strings.game_close_warning),
            onDismissClick = { component.onEvent(GameStore.Intent.DismissCloseDialog) },
            onConfirmClick = { component.onEvent(GameStore.Intent.ConfirmCloseDialog) },
            dismissButtonText = stringResource(MR.strings.cancel),
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
fun GameLayout(
    state: GameStore.State,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            with(LocalDensity.current) {
                state.currentTarget?.let { target ->
                    val targetCoordinate = target.toGameScreenCoordinate(maxWidth.roundToPx(), maxHeight.roundToPx())
                    TargetLayout(Modifier.offset(targetCoordinate.x.toDp(), targetCoordinate.y.toDp()))
                }

                val cursorCoordinate = state.cursor.toGameScreenCoordinate(maxWidth.roundToPx(), maxHeight.roundToPx())
                CursorLayout(Modifier.offset(cursorCoordinate.x.toDp(), cursorCoordinate.y.toDp()))
            }
        }

        ScoreLayout(
            score = state.score,
            modifier = Modifier.align(Alignment.TopStart)
        )

        TimeLayout(
            millis = state.millis,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun TargetLayout(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = DeskMotionIcon.Target,
        contentDescription = null,
        tint = DeskMotionTheme.colors.primary,
        modifier = modifier
            .size(150.dp)
    )
}

@Composable
private fun CursorLayout(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = DeskMotionIcon.Plus,
        contentDescription = null,
        tint = DeskMotionTheme.colors.inversePrimary,
        modifier = modifier
            .size(100.dp)
    )
}

@Composable
private fun ScoreLayout(
    score: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(DeskMotionDimension.layoutMainMargin)
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = DeskMotionDimension.layoutMainMargin,
                vertical = DeskMotionDimension.layoutMediumMargin
            )
    ) {
        Text(
            text = stringResource(MR.strings.game_score, score.toString()),
            style = DeskMotionTypography.textBook24,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TimeLayout(
    millis: Long,
    modifier: Modifier = Modifier
) {
    val minutesFormatted = (millis / (1000 * 60)).toString().padStart(2, '0')
    val secondsFormatted = (millis / 1000 % 60).toString().padStart(2, '0')
    val millisFormatted = (millis % 1000).toString().padStart(3, '0')

    Row(
        modifier = modifier
            .padding(DeskMotionDimension.layoutMainMargin)
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = DeskMotionDimension.layoutMainMargin,
                vertical = DeskMotionDimension.layoutMediumMargin
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = DeskMotionIcon.Clock,
            contentDescription = null,
            tint = DeskMotionTheme.colors.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )

        HSpacer(DeskMotionDimension.layoutMediumMargin)

        Text(
            text = "$minutesFormatted:$secondsFormatted.$millisFormatted",
            style = DeskMotionTypography.textBook24,
            color = DeskMotionTheme.colors.onSurfaceVariant
        )
    }
}
