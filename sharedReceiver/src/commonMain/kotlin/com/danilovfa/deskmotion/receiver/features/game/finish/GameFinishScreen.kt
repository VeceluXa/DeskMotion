package com.danilovfa.deskmotion.receiver.features.game.finish

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.game.finish.store.GameFinishStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.buttons.Button
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.text.Text
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GameFinishScreen(component: GameFinishComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameFinishLayout(component, state)

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(GameFinishStore.Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(GameFinishStore.Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
private fun GameFinishLayout(component: GameFinishComponent, state: GameFinishStore.State) {
    Column(
        modifier = Modifier
            .padding(vertical = DeskMotionDimension.layoutExtraLargeMargin)
            .fillMaxHeight()
            .fillMaxWidth(0.4f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(MR.strings.victory),
            color = DeskMotionTheme.colors.onBackground,
            style = DeskMotionTypography.titleBook34
        )

        VSpacer(DeskMotionDimension.layoutLargeMargin)

        Text(
            text = stringResource(MR.strings.total_score, state.playLog.score.toString()),
            color = DeskMotionTheme.colors.onBackground,
            style = DeskMotionTypography.textBook24
        )

        VSpacer(DeskMotionDimension.layoutExtraLargeMargin)
        VSpacer(DeskMotionDimension.layoutExtraLargeMargin)
        VSpacer(DeskMotionDimension.layoutExtraLargeMargin)

        Button(
            text = stringResource(MR.strings.ok),
            loading = state.isLoading,
            onClick = { component.onEvent(GameFinishStore.Intent.OnOkClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}