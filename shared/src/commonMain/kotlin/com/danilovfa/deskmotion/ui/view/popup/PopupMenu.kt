package com.danilovfa.deskmotion.ui.view.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.text.Text

@Composable
fun PopupMenu(
    menuItemsData: List<MenuItemsData>,
    expandedState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier.background(DeskMotionTheme.colors.background),
        expanded = expandedState.value,
        onDismissRequest = { expandedState.value = false }
    ) {
        menuItemsData.forEach { item ->
            MenuItems(
                title = item.title,
                icon = item.icon,
                tint = item.tint(),
                onClick = {
                    item.onClick.invoke()
                    expandedState.value = false
                },
            )
        }
    }
}

data class MenuItemsData(
    val title: String,
    val onClick: () -> Unit,
    val tint: @Composable () -> Color = { DeskMotionTheme.colors.onBackground },
    val icon: Painter? = null
)

@Composable
private fun MenuItems(
    title: String,
    icon: Painter?,
    onClick: () -> Unit,
    tint: Color = DeskMotionTheme.colors.onBackground
) {
    DropdownMenuItem(
        text = { GroupActionsItem(text = title, icon = icon, tint = tint) },
        onClick = onClick
    )
}

@Composable
private fun GroupActionsItem(
    text: String,
    icon: Painter?,
    tint: Color = DeskMotionTheme.colors.onBackground,
) {
    Row(
        modifier = Modifier.padding(end = DeskMotionDimension.layoutMediumMargin)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(24.dp),
                painter = icon,
                contentDescription = null,
                tint = tint
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = DeskMotionDimension.layoutMainMargin)
                .align(Alignment.CenterVertically),
            text = text,
            style = DeskMotionTypography.textBook18,
            color = tint
        )
    }
}