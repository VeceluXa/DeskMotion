package com.danilovfa.deskmotion.ui.theme.color

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class DeskMotionThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val error: Color,
    val errorContainer: Color,
    val onError: Color,
    val onErrorContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val inverseOnSurface: Color,
    val inverseSurface: Color,
    val inversePrimary: Color,
    val surfaceTint: Color,
    val outlineVariant: Color,
    val scrim: Color,
    val asterisk: Color,
    val shimmerGradient: GradientColor
)

@Stable
data class GradientColor(
    val colorStart: Color,
    val colorCenter: Color,
    val colorEnd: Color
)