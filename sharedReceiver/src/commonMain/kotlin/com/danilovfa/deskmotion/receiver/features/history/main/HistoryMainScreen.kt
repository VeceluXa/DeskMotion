package com.danilovfa.deskmotion.receiver.features.history.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.features.game.game.store.GameStore
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.WSpacer
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.stub.EmptyStub
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import com.danilovfa.deskmotion.utils.time.formattedDateTime
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HistoryMainScreen(component: HistoryMainComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.history)
        )

        HistoryMainLayout(component, state)

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(HistoryMainStore.Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(HistoryMainStore.Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
private fun HistoryMainLayout(
    component: HistoryMainComponent,
    state: HistoryMainStore.State
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(DeskMotionDimension.layoutLargeMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(state.logs) { log ->
            LogItem(
                log = log,
                isTheOnlyChild = state.isTheOnlyChild,
                onClick = { component.onEvent(HistoryMainStore.Intent.OnPlayLogClicked(log)) }
            )
        }

        item {
            if (state.isEmpty) {
                EmptyStub(
                    title = stringResource(MR.strings.history_empty_stub_title),
                    message = stringResource(MR.strings.history_empty_stub_description),
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(DeskMotionDimension.layoutLargeMargin)
                )
            }

            LoaderLayout(
                showLoader = state.isLoading,
                modifier = Modifier.fillParentMaxSize()
            )
        }
    }
}

@Composable
private fun LogItem(
    log: PlayLog,
    isTheOnlyChild: Boolean,
    onClick: () -> Unit
) {
    val time = formattedDateTime(log.completedEpochMillis)
    val firstText = if (isTheOnlyChild) {
        time
    } else {
        ""
//        "${log.firstName} ${log.lastName} ${log.middleName}"
    }

    val secondText = if (isTheOnlyChild) {
        stringResource(MR.strings.game_score, log.score.toString())
    } else {
        time
    }

    Row(
        modifier = Modifier
            .padding(DeskMotionDimension.layoutMediumMargin)
            .fillMaxWidth(0.6f)
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .padding(
                horizontal = DeskMotionDimension.layoutHorizontalMargin,
                vertical = DeskMotionDimension.layoutMediumMargin
            )
    ) {
        Text(
            text = firstText,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )

        WSpacer()

        Text(
            text = secondText,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )
    }
}