package com.danilovfa.deskmotion.ui.theme.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal object DeskMotionColor {
    internal val md_theme_light_primary = Color(0xFF146E00)
    internal val md_theme_light_onPrimary = Color(0xFFFFFFFF)
    internal val md_theme_light_primaryContainer = Color(0xFF7FFE5F)
    internal val md_theme_light_onPrimaryContainer = Color(0xFF022100)
    internal val md_theme_light_secondary = Color(0xFF54624D)
    internal val md_theme_light_onSecondary = Color(0xFFFFFFFF)
    internal val md_theme_light_secondaryContainer = Color(0xFFD7E8CC)
    internal val md_theme_light_onSecondaryContainer = Color(0xFF121F0E)
    internal val md_theme_light_tertiary = Color(0xFF386668)
    internal val md_theme_light_onTertiary = Color(0xFFFFFFFF)
    internal val md_theme_light_tertiaryContainer = Color(0xFFBCEBED)
    internal val md_theme_light_onTertiaryContainer = Color(0xFF002021)
    internal val md_theme_light_error = Color(0xFFBA1A1A)
    internal val md_theme_light_errorContainer = Color(0xFFFFDAD6)
    internal val md_theme_light_onError = Color(0xFFFFFFFF)
    internal val md_theme_light_onErrorContainer = Color(0xFF410002)
    internal val md_theme_light_background = Color(0xFFFDFDF6)
    internal val md_theme_light_onBackground = Color(0xFF1A1C18)
    internal val md_theme_light_surface = Color(0xFFFDFDF6)
    internal val md_theme_light_onSurface = Color(0xFF1A1C18)
    internal val md_theme_light_surfaceVariant = Color(0xFFDFE4D7)
    internal val md_theme_light_onSurfaceVariant = Color(0xFF43483F)
    internal val md_theme_light_outline = Color(0xFF73796E)
    internal val md_theme_light_inverseOnSurface = Color(0xFFF1F1EA)
    internal val md_theme_light_inverseSurface = Color(0xFF2F312D)
    internal val md_theme_light_inversePrimary = Color(0xFF63E145)
    internal val md_theme_light_shadow = Color(0xFF000000)
    internal val md_theme_light_surfaceTint = Color(0xFF146E00)
    internal val md_theme_light_outlineVariant = Color(0xFFC3C8BC)
    internal val md_theme_light_scrim = Color(0xFF000000)
    internal val md_theme_light_shimmer_start = Color(0xFFF2F2F2)
    internal val md_theme_light_shimmer_center = Color(0xFFF8F8F8)
    internal val md_theme_light_shimmer_end = Color(0xFFEBEBEB)

    internal val md_theme_dark_primary = Color(0xFF63E145)
    internal val md_theme_dark_onPrimary = Color(0xFF063900)
    internal val md_theme_dark_primaryContainer = Color(0xFF0D5300)
    internal val md_theme_dark_onPrimaryContainer = Color(0xFF7FFE5F)
    internal val md_theme_dark_secondary = Color(0xFFBBCBB1)
    internal val md_theme_dark_onSecondary = Color(0xFF273421)
    internal val md_theme_dark_secondaryContainer = Color(0xFF3D4B36)
    internal val md_theme_dark_onSecondaryContainer = Color(0xFFD7E8CC)
    internal val md_theme_dark_tertiary = Color(0xFFA0CFD1)
    internal val md_theme_dark_onTertiary = Color(0xFF003739)
    internal val md_theme_dark_tertiaryContainer = Color(0xFF1E4E50)
    internal val md_theme_dark_onTertiaryContainer = Color(0xFFBCEBED)
    internal val md_theme_dark_error = Color(0xFFFFB4AB)
    internal val md_theme_dark_errorContainer = Color(0xFF93000A)
    internal val md_theme_dark_onError = Color(0xFF690005)
    internal val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
    internal val md_theme_dark_background = Color(0xFF1A1C18)
    internal val md_theme_dark_onBackground = Color(0xFFE2E3DC)
    internal val md_theme_dark_surface = Color(0xFF1A1C18)
    internal val md_theme_dark_onSurface = Color(0xFFE2E3DC)
    internal val md_theme_dark_surfaceVariant = Color(0xFF43483F)
    internal val md_theme_dark_onSurfaceVariant = Color(0xFFC3C8BC)
    internal val md_theme_dark_outline = Color(0xFF8D9387)
    internal val md_theme_dark_inverseOnSurface = Color(0xFF1A1C18)
    internal val md_theme_dark_inverseSurface = Color(0xFFE2E3DC)
    internal val md_theme_dark_inversePrimary = Color(0xFF146E00)
    internal val md_theme_dark_shadow = Color(0xFF000000)
    internal val md_theme_dark_surfaceTint = Color(0xFF63E145)
    internal val md_theme_dark_outlineVariant = Color(0xFF43483F)
    internal val md_theme_dark_scrim = Color(0xFF000000)
    internal val md_theme_dark_shimmer_start = Color(0xFF161616)
    internal val md_theme_dark_shimmer_center = Color(0xFF2A2A2A)
    internal val md_theme_dark_shimmer_end = Color(0xFF0C0C0C)
}

@Composable
private fun ColorPreview(color: Color, name: String) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(color)
            .padding(8.dp)
    ) {
        val textColor = if (color.luminance() < 0.5) Color.White else Color.Black
        Text(text = name, fontSize = 10.sp, color = textColor)
    }
}
