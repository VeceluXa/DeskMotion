package com.danilovfa.deskmotion.sender.features.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.model.TransferEvent
import com.danilovfa.deskmotion.sender.features.game.store.GameStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.animation.IconAnimatedVisibility
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.utils.time.formattedEpochMillis
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GameScreen(component: GameComponent) {
    val state by component.state.collectAsState()
    GameLayout(component, state)
}

@Composable
private fun GameLayout(component: GameComponent, state: GameStore.State) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
            .systemBarsPadding()
    ) {
        Toolbar(
            title = stringResource(MR.strings.app_name),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { component.onEvent(GameStore.Intent.OnBackButtonClicked) }
        )

        LogLayout(state, Modifier.weight(1f))

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
private fun LogLayout(
    state: GameStore.State,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    var doScrollToTop by remember { mutableStateOf(false) }

    LaunchedEffect(doScrollToTop) {
        if (state.logs.isNotEmpty()) {
            listState.animateScrollToItem(0)
            doScrollToTop = false
        }
    }

    val isScrollButtonVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.logs.size) { position ->
                val index = state.logs.size - position
                LogItem(index, state.logs[position])
            }
            
            item {
                VSpacer(DeskMotionDimension.minTouchSize + DeskMotionDimension.layoutLargeMargin)
            }
        }

        IconAnimatedVisibility(
            visible = isScrollButtonVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(DeskMotionDimension.layoutLargeMargin)
                .size(DeskMotionDimension.minTouchSize)
        ) {
            IconButton(
                onClick = {
                    doScrollToTop = true
                },
                modifier = Modifier
                    .background(
                        color = DeskMotionTheme.colors.primary,
                        shape = CircleShape
                    )
                    .fillMaxSize()
            ) {
                Icon(
                    painter = DeskMotionIcon.ArrowUp,
                    tint = DeskMotionTheme.colors.onPrimary,
                    contentDescription = stringResource(MR.strings.scroll_bottom)
                )
            }
        }
    }
}

@Composable
private fun LogItem(index: Int, logItem: TransferEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(24.dp)
    ) {
        Text(
            text = index.toString(),
            color = DeskMotionTheme.colors.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(52.dp)
        )
        HSpacer(DeskMotionDimension.layoutMainMargin)
        Text(
            text = logItem.toString(),
            modifier = Modifier.weight(1f),
            color = DeskMotionTheme.colors.onBackground
        )
        HSpacer(DeskMotionDimension.layoutMainMargin)
        Text(
            text = formattedEpochMillis(logItem.epochMillis),
            color = DeskMotionTheme.colors.onBackground
        )
    }
}