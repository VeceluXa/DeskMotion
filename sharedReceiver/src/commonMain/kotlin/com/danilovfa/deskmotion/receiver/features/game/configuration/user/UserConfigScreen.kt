package com.danilovfa.deskmotion.receiver.features.game.configuration.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore
import com.danilovfa.deskmotion.receiver.features.game.configuration.user.store.UserConfigStore.Intent
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.animation.IconAnimatedVisibility
import com.danilovfa.deskmotion.ui.view.buttons.PrimaryButtonLarge
import com.danilovfa.deskmotion.ui.view.buttons.TextButton
import com.danilovfa.deskmotion.ui.view.dialog.AlertDialog
import com.danilovfa.deskmotion.ui.view.text.LargeTextField
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Composable
fun UserConfigScreen(component: UserConfigComponent) {
    val state by component.stateFlow.collectAsState()

    UserConfigLayout(
        state = state,
        onIntent = component::onIntent
    )
}

@Composable
private fun UserConfigLayout(
    state: UserConfigStore.State,
    onIntent: (Intent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = stringResource(MR.strings.wifi),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { onIntent(Intent.OnBackClicked) }
        )
        UserConfig(state, onIntent)
    }
}

@Composable
private fun UserConfig(state: UserConfigStore.State, onIntent: (Intent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.4f)
    ) {
        VSpacer(DeskMotionDimension.layoutLargeMargin)
        LargeTextField(
            value = state.firstName,
            onValueChange = { onIntent(Intent.OnFirstNameChanged(it)) },
            labelText = stringResource(MR.strings.first_name),
            isRequired = true
        )
        VSpacer(DeskMotionDimension.layoutMainMargin)
        LargeTextField(
            value = state.lastName,
            onValueChange = { onIntent(Intent.OnLastNameChanged(it)) },
            labelText = stringResource(MR.strings.last_name),
            isRequired = true
        )
        VSpacer(DeskMotionDimension.layoutMainMargin)
        LargeTextField(
            value = state.middleName,
            onValueChange = { onIntent(Intent.OnMiddleNameChanged(it)) },
            labelText = stringResource(MR.strings.middle_name),
            isRequired = false
        )
        VSpacer(DeskMotionDimension.layoutMainMargin)
        DateOfBirthField(
            dateOfBirth = state.dateOfBirth,
            onClick = { onIntent(Intent.OnDateOfBirthClicked) }
        )

        VSpacer(DeskMotionDimension.layoutLargeMargin)
        PrimaryButtonLarge(
            text = stringResource(MR.strings.save),
            onClick = { onIntent(Intent.OnNextClicked) },
            enabled = state.isNextButtonEnabled,
            modifier = Modifier.padding(DeskMotionDimension.layoutHorizontalMargin)
        )

        AlertDialog(
            isVisible = state.isError,
            title = stringResource(MR.strings.error),
            text = state.errorMessage,
            onDismissClick = { onIntent(Intent.OnErrorDismissed) },
            onConfirmClick = { onIntent(Intent.OnErrorDismissed) },
            confirmButtonText = stringResource(MR.strings.ok)
        )

        DateOfBirthPicker(
            dateOfBirth = state.dateOfBirth,
            isVisible = state.isDatePickerVisible,
            onDismiss = { onIntent(Intent.OnDatePickerDismissed) },
            onConfirm = { onIntent(Intent.OnDatePickerConfirmed(it)) }
        )
    }
}

@Composable
private fun DateOfBirthField(
    dateOfBirth: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = dateOfBirth?.let { "${it.dayOfMonth}.${it.monthNumber}.${it.year}" }
        ?: stringResource(MR.strings.date_of_birth)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = DeskMotionTheme.colors.secondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                brush = SolidColor(DeskMotionTheme.colors.secondary),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            color = DeskMotionTheme.colors.onSecondaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateOfBirthPicker(
    dateOfBirth: LocalDate?,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dateOfBirth?.atStartOfDayIn(TimeZone.currentSystemDefault())
            ?.toEpochMilliseconds()
    )
    val pickedDate by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis?.let { dateMillis ->
                val dateTime = Instant
                    .fromEpochMilliseconds(dateMillis)
                    .toLocalDateTime(TimeZone.currentSystemDefault())

                LocalDate(dateTime.year, dateTime.monthNumber, dateTime.dayOfMonth)
            }
        }
    }

    IconAnimatedVisibility(isVisible) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    text = stringResource(MR.strings.ok),
                    onClick = {
                        pickedDate?.let(onConfirm)
                    }
                )
            },
            modifier = modifier
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}