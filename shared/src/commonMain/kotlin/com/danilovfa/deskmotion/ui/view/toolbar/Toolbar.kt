package com.danilovfa.deskmotion.ui.view.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.modifier.surface
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.animation.AnimatedVisibilityNullableValue
import com.danilovfa.deskmotion.ui.view.animation.IconAnimatedVisibility
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    enableShadow: Boolean = false
) {
    Toolbar(
        title = title,
        navigationIcon = null,
        onNavigationClick = {},
        modifier = modifier,
        enableShadow = enableShadow
    )
}

@Composable
fun Toolbar(
    navigationIcon: NavigationIcon?,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = DeskMotionTheme.colors.secondaryContainer,
    title: String = "",
    enableShadow: Boolean = false
) {
    val elevation = if (enableShadow) 6.dp else 0.dp
    val bottomPadding = if (enableShadow) 2.dp else 0.dp
    Toolbar(
        modifier = modifier
            .padding(bottom = bottomPadding)
            .shadow(elevation),
        color = color,
    ) {
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            NavigationIcon(navigationIcon, onNavigationClick)

            Column(
                Modifier
                    .padding(horizontal = DeskMotionDimension.layoutHorizontalMargin)
                    .weight(1f),
            ) {
                Text(
                    text = title,
                    style = DeskMotionTypography.textMedium18,
                    color = DeskMotionTheme.colors.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
@Suppress("ReusedModifierInstance")
fun Toolbar(
    navigationIcon: NavigationIcon?,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = DeskMotionTheme.colors.secondaryContainer,
    actions: @Composable RowScope.() -> Unit = {},
    contentBelowToolbar: @Composable (ColumnScope.() -> Unit)? = null,
    title: String = "",
    enableShadow: Boolean = false,
    isActionsVisible: Boolean = true,
) {
    val elevation = if (enableShadow) 6.dp else 0.dp
    val bottomPadding = if (enableShadow) 2.dp else 0.dp

    Column {
        Toolbar(
            modifier = modifier
                .padding(bottom = bottomPadding)
                .shadow(elevation = elevation),
            color = color
        ) {
            Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
                NavigationIcon(navigationIcon, onNavigationClick)

                Column(
                    Modifier
                        .padding(horizontal = DeskMotionDimension.layoutHorizontalMargin)
                        .weight(1f),
                ) {
                    Text(text = title, style = DeskMotionTypography.textMedium18)
                }

                IconAnimatedVisibility(visible = isActionsVisible) {
                    Row(
                        modifier = Modifier.padding(end = IconCornerPadding),
                        content = actions,
                    )
                }
            }
        }

        contentBelowToolbar?.invoke(this)
    }
}

@Composable
private fun NavigationIcon(
    navigationIcon: NavigationIcon?,
    onNavigationClick: () -> Unit,
) {
    AnimatedVisibilityNullableValue(navigationIcon) { icon ->
        IconButton(
            onClick = onNavigationClick,
            modifier = Modifier
                .padding(start = IconCornerPadding - icon.innerPadding),
            enabled = navigationIcon != null,
            content = { icon() },
        )
    }
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    color: Color = DeskMotionTheme.colors.secondaryContainer,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .surface(backgroundColor = color, shape = RectangleShape)
            .padding(contentPadding)
            .fillMaxWidth()
            .height(AppBarHeight)
            .padding(bottom = 4.dp),
        contentAlignment = Alignment.CenterStart,
        content = content,
    )
}

val AppBarHeight: Dp = 56.dp
private val IconCornerPadding =
    DeskMotionDimension.toolbarHorizontalMargin - (DeskMotionDimension.minTouchSize - 32.dp) / 2

enum class NavigationIcon(
    private val painter: @Composable () -> Painter,
    val innerPadding: Dp,
) {
    Back(
        painter = { DeskMotionIcon.Back },
        innerPadding = 8.dp,
    ),

    Close(
        painter = { DeskMotionIcon.Close },
        innerPadding = 8.dp,
    ),

    Settings(
        painter = { DeskMotionIcon.Settings },
        innerPadding = 8.dp,
    );

    @Composable
    operator fun invoke() {
        Icon(
            painter = painter(),
            contentDescription = null,
            tint = DeskMotionTheme.colors.onSecondaryContainer,
            modifier = Modifier.size(24.dp)
        )
    }
}