package com.danilovfa.deskmotion.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.danilovfa.deskmotion.ui.theme.color.DeskMotionColorDarkPalette
import com.danilovfa.deskmotion.ui.theme.color.DeskMotionColorLightPalette
import com.danilovfa.deskmotion.ui.theme.color.DeskMotionThemeColors

@Composable
fun DeskMotionTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorPalette = remember(useDarkTheme) {
        if (useDarkTheme) DeskMotionColorDarkPalette else DeskMotionColorLightPalette
    }

    MaterialTheme(
        typography = MaterialTypography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(
            LocalDeskMotionColors provides colorPalette,
            LocalTextStyle provides DeskMotionTypography.textBook18,
            LocalContentColor provides colorPalette.onSurface,
            content = content
        )
    }
}

internal val LocalDeskMotionColors = staticCompositionLocalOf<DeskMotionThemeColors> {
    error("No DeskMotionColors provided")
}