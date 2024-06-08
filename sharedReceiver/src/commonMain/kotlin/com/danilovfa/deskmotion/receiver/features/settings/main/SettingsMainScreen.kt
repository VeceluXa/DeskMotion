package com.danilovfa.deskmotion.receiver.features.settings.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore
import com.danilovfa.deskmotion.receiver.features.settings.main.store.SettingsMainStore.Intent
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.WSpacer
import com.danilovfa.deskmotion.ui.view.buttons.Button
import com.danilovfa.deskmotion.ui.view.buttons.OutlinedButton
import com.danilovfa.deskmotion.ui.view.popup.MenuItemsData
import com.danilovfa.deskmotion.ui.view.popup.PopupMenu
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.receiver.utils.locale.DeskMotionLocale
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.buttons.DeskMotionButtonColors
import com.danilovfa.deskmotion.ui.view.buttons.ButtonLarge
import com.danilovfa.deskmotion.ui.view.text.TextField

@Composable
fun SettingsMainScreen(component: SettingsMainComponent) {
    val state by component.state.collectAsState()
    SettingsMainLayout(state, component)
}

@Composable
private fun SettingsMainLayout(state: SettingsMainStore.State, component: SettingsMainComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(title = stringResource(MR.strings.settings))
        Column(
            modifier = Modifier
                .width(900.dp)
                .padding(vertical = DeskMotionDimension.layoutLargeMargin)
        ) {
            LanguagesDropDown(
                currentItem = state.locale,
                onSelected = { component.onEvent(Intent.OnLanguageSelected(it)) },
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            VSpacer(8.dp)

            NetworkSettings(state, component)

            VSpacer(8.dp)

            UserSettings(
                onClick = { component.onEvent(Intent.OnUserConfigClicked) }
            )

            VSpacer(32.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    text = stringResource(MR.strings.save),
                    onClick = { component.onEvent(Intent.OnSaveClicked) },
                    enabled = state.isSaveButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun UserSettings(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ButtonLarge(
        text = stringResource(MR.strings.settings_user),
        onClick = onClick,
        colors = DeskMotionButtonColors.secondaryButtonColors(),
        modifier = modifier
    )
}

@Composable
private fun NetworkSettings(
    state: SettingsMainStore.State,
    component: SettingsMainComponent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = state.ip,
            onValueChange = { component.onEvent(Intent.OnIpChanged(it)) },
            labelText = stringResource(MR.strings.ip)
        )
        WSpacer()
        TextField(
            value = state.port,
            onValueChange = { component.onEvent(Intent.OnPortChanged(it)) },
            labelText = stringResource(MR.strings.port)
        )
    }
}

@Composable
private fun LanguagesDropDown(
    currentItem: DeskMotionLocale?,
    onSelected: (DeskMotionLocale) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItemsData = DeskMotionLocale.entries.map { locale ->
        MenuItemsData(
            title = locale.localeName,
            onClick = { onSelected(locale) }
        )
    }

    SettingsDropDown(
        name = stringResource(MR.strings.language),
        currentItem = currentItem?.localeName,
        items = menuItemsData,
        modifier = modifier
    )
}

@Composable
private fun SettingsDropDown(
    name: String,
    currentItem: String?,
    items: List<MenuItemsData>,
    modifier: Modifier = Modifier
) {
    val expandedState = remember { mutableStateOf(false) }
    SettingsContainer(
        modifier = modifier
    ) {
        Text(
            text = name,
            color = DeskMotionTheme.colors.onSurface,
            style = DeskMotionTypography.textBook24
        )
        WSpacer(Modifier.width(48.dp))
        Column {
            OutlinedButton(
                text = currentItem ?: "",
                onClick = { expandedState.value = true },
                modifier = Modifier.width(150.dp)
            )
            PopupMenu(
                menuItemsData = items,
                expandedState = expandedState
            )
        }
    }
}

@Composable
private fun SettingsContainer(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DeskMotionDimension.layoutMainMargin)
            .background(DeskMotionTheme.colors.surface, RoundedCornerShape(24.dp))
            .padding(DeskMotionDimension.layoutMediumMargin),
    ) {
        content()
    }
}