package com.danilovfa.deskmotion.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

val MaterialTypography = Typography(
    displayLarge = DeskMotionTypography.titleBook44,
    displayMedium = DeskMotionTypography.titleBook34
)

object DeskMotionTypography {
    private val defaultStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        platformStyle = PlatformTextStyle(null, null)
    )

    val titleBook44 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 44.sp,
        lineHeight = 48.sp,
    )
    val titleBook34 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 40.sp,
    )
    val titleBook28 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    )
    val titleBook28_32 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    )
    val textBook23 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 23.sp,
        lineHeight = 28.sp,
    )
    val textBook24 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    )
    val textMedium24 = defaultStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    )
    val textBook14 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 30.sp,
    )
    val textMedium18 = defaultStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )
    val textBook18 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    )
    val textBook18Underlined = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        textDecoration = TextDecoration.Underline
    )
    val captionMedium16 = defaultStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )
    val captionBook16 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    )
    val captionMedium14 = defaultStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 16.sp,
    )
    val captionBook14 = defaultStyle.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 16.sp,
    )
    val captionMedium12 = defaultStyle.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 14.sp,
    )
}