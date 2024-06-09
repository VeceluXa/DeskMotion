package com.danilovfa.deskmotion.receiver.features.history.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.model.User
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainPage
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainUserSort
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore.Intent
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore.State
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.WSpacer
import com.danilovfa.deskmotion.ui.view.buttons.FloatTextButton
import com.danilovfa.deskmotion.ui.view.buttons.TextButton
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.popup.MenuItemsData
import com.danilovfa.deskmotion.ui.view.popup.PopupMenu
import com.danilovfa.deskmotion.ui.view.search.Search
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.stub.EmptyStub
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import com.danilovfa.deskmotion.utils.time.formatted
import com.danilovfa.deskmotion.utils.time.formattedDateTime
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HistoryMainScreen(component: HistoryMainComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.history),
            navigationIcon = if (state.isBackButtonVisible) NavigationIcon.Back else null,
            onNavigationClick = { if (state.isBackButtonVisible) component.onEvent(Intent.OnBackButtonClicked) }
        )

        AnimatedVisibility(
            visible = state.isSearchQueryVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            ChildrenSearchAndSortLayout(component, state)
        }

        HistoryMainLayout(component, state)

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { component.onEvent(Intent.DismissErrorDialog) },
            onConfirmClick = { component.onEvent(Intent.DismissErrorDialog) },
            confirmButtonText = stringResource(MR.strings.ok)
        )
    }
}

@Composable
private fun ChildrenSearchAndSortLayout(
    component: HistoryMainComponent,
    state: State,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = DeskMotionTheme.colors.secondaryContainer)
            .padding(
                horizontal = DeskMotionDimension.layoutHorizontalMargin
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Search(
            value = state.userSearchQuery,
            onValueChange = { component.onEvent(Intent.OnUserSearchQueryChanged(it)) },
            onValueResetClick = { component.onEvent(Intent.OnUserSearchQueryChanged("")) },
            placeholderText = stringResource(MR.strings.search),
            modifier = Modifier.weight(1f)
        )

        HSpacer(DeskMotionDimension.layoutExtraLargeMargin)

        TextButton(
            text = if (state.userSort.isDescending) {
                stringResource(MR.strings.descending)
            }
            else {
                stringResource(MR.strings.ascending)
            },
            onClick = {
                component.onEvent(
                    Intent.OnSortChanged(state.userSort.copySort(state.userSort.isDescending.not()))
                )
            }
        )

        HSpacer(DeskMotionDimension.layoutMainMargin)
        SortDropdown(state, component)
    }
}

@Composable
private fun SortDropdown(state: State, component: HistoryMainComponent) {
    val expandedState = remember { mutableStateOf(false) }
    val menuItems = listOf(
        MenuItemsData(
            title = stringResource(MR.strings.last_name),
            onClick = { component.onEvent(Intent.OnSortChanged(HistoryMainUserSort.LastName(state.userSort.isDescending))) }
        ),
        MenuItemsData(
            title = stringResource(MR.strings.first_name),
            onClick = { component.onEvent(Intent.OnSortChanged(HistoryMainUserSort.FirstName(state.userSort.isDescending))) }
        ),
        MenuItemsData(
            title = stringResource(MR.strings.middle_name),
            onClick = { component.onEvent(Intent.OnSortChanged(HistoryMainUserSort.MiddleName(state.userSort.isDescending))) }
        ),
        MenuItemsData(
            title = stringResource(MR.strings.date_of_birth),
            onClick = { component.onEvent(Intent.OnSortChanged(HistoryMainUserSort.DateOfBirth(state.userSort.isDescending))) }
        ),
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(MR.strings.sort_by),
            style = DeskMotionTypography.textMedium18,
            color = DeskMotionTheme.colors.onSecondaryContainer
        )
        Column {
            TextButton(
                text = stringResource(state.userSort.titleRes),
                onClick = { expandedState.value = true }
            )
            PopupMenu(
                menuItemsData = menuItems,
                expandedState = expandedState
            )
        }
    }

}

@Composable
private fun HistoryMainLayout(
    component: HistoryMainComponent,
    state: State
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(DeskMotionDimension.layoutLargeMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.currentPage == HistoryMainPage.CHILDREN) {
            items(
                items = state.transformedUsers,
                key = { it.id }
            ) { user ->
                UserItem(
                    user = user,
                    onClick = { component.onEvent(Intent.OnUserClicked(user)) }
                )
            }
        } else {
            items(
                items = state.transformedLogs,
                key = { it.id }
            ) { log ->
                PlayLogItem(
                    log = log,
                    onClick = { component.onEvent(Intent.OnPlayLogClicked(log)) }
                )
            }
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
private fun UserItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val name = "${user.lastName} ${user.firstName} ${user.middleName}".trim()

    Row(
        modifier = modifier
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
            text = name,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )

        WSpacer()

        Text(
            text = user.dateOfBirth.formatted(),
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )
    }
}

@Composable
private fun PlayLogItem(
    log: PlayLog,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val time = formattedDateTime(log.completedEpochMillis)
    val score = stringResource(MR.strings.game_score, log.score.toString())

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
            text = time,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )

        WSpacer()

        Text(
            text = score,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            style = DeskMotionTypography.textBook24
        )
    }
}