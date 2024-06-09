package com.danilovfa.deskmotion.ui.view.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.compose.painterResource
import com.danilovfa.deskmotion.MR


object DeskMotionIcon {
    val CheckMark: Painter @Composable get() = painterResource(MR.images.icon_checkmark)
    val Back: Painter @Composable get() = painterResource(MR.images.icon_arrow_left)
    val Minimize: Painter @Composable get() = painterResource(MR.images.icon_minus)
    val Maximize: Painter @Composable get() = painterResource(MR.images.icon_square)
    val Close: Painter @Composable get() = painterResource(MR.images.icon_close)
    val Settings: Painter @Composable get() = painterResource(MR.images.icon_settings)
    val ArrowUp: Painter @Composable get() = painterResource(MR.images.icon_arrow_up)
    val ArrowDown: Painter @Composable get() = painterResource(MR.images.icon_arrow_down)
    val Menu: Painter @Composable get() = painterResource(MR.images.icon_menu)
    val Play: Painter @Composable get() = painterResource(MR.images.icon_play)
    val History: Painter @Composable get() = painterResource(MR.images.icon_calendar)
    val WiFi: Painter @Composable get() = painterResource(MR.images.icon_wifi)
    val Bluetooth: Painter @Composable get() = painterResource(MR.images.icon_bluetooth)
    val Clock: Painter @Composable get() = painterResource(MR.images.icon_clock)
    val Target: Painter @Composable get() = painterResource(MR.images.icon_target)
    val Plus: Painter @Composable get() = painterResource(MR.images.icon_plus)
    val Search: Painter @Composable get() = painterResource(MR.images.icon_search)
    val Delete: Painter @Composable get() = painterResource(MR.images.icon_delete)
}