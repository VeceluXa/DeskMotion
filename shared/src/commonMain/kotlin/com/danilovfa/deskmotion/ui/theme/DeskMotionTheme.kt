package com.danilovfa.deskmotion.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.danilovfa.deskmotion.ui.theme.color.DeskMotionThemeColors

object DeskMotionTheme {
    val colors: DeskMotionThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDeskMotionColors.current
}