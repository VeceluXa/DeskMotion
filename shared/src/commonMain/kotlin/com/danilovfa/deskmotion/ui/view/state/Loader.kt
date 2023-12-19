package com.danilovfa.deskmotion.ui.view.state

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.modifier.noRippleClickable
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    color: Color = DeskMotionTheme.colors.primary,
    strokeWidth: Dp = 4.dp,
) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = strokeWidth,
        modifier = modifier.size(40.dp),
    )
}

@Composable
@Suppress("ReusedModifierInstance")
fun LoaderLayout(
    modifier: Modifier = Modifier,
    showLoader: Boolean = false,
    backgroundColor: Color = Color.Black.copy(alpha = 0.4f),
    loaderBackgroundColor: Color = DeskMotionTheme.colors.background
) {
    AnimatedVisibility(
        visible = showLoader,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .noRippleClickable { },
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
                    .background(color = loaderBackgroundColor, shape = CircleShape),
            ) {
                Loader(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}