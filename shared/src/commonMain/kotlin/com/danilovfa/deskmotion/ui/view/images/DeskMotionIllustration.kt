package com.danilovfa.deskmotion.ui.view.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.danilovfa.deskmotion.MR
import dev.icerock.moko.resources.compose.painterResource

object DeskMotionIllustration {
    val Empty: Painter @Composable get() = painterResource(MR.images.image_empty)
}