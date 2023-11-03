package com.danilovfa.deskmotion.ui.view.state

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.largeShimmer
import com.danilovfa.deskmotion.ui.theme.micro

@Composable
fun ShimmerItem(
    size: DpSize,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.micro,
) {
    Box(
        modifier = modifier
            .size(size.width, size.height)
            .background(getShimmerBrush(), shape),
    )
}

@Composable
fun LargeShimmerItem(
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.largeShimmer,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(getShimmerBrush(), shape),
    )
}

@Composable
fun CircleShimmerItem(
    shape: Shape = CircleShape,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(getShimmerBrush(), shape),
    )
}

@Composable
fun CheckBoxShimmerItem() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(getShimmerBrush(), CircleShape)
            .padding(2.dp)
            .background(DeskMotionTheme.colors.background, CircleShape),
    )
}

@Composable
private fun getShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0F,
        targetValue = 1000F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    return Brush.linearGradient(
        colors = getShimmerColors(),
        start = Offset(10F, 10F),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
private fun getShimmerColors() = listOf(
    DeskMotionTheme.colors.shimmerGradient.colorStart,
    DeskMotionTheme.colors.shimmerGradient.colorCenter,
    DeskMotionTheme.colors.shimmerGradient.colorEnd,
)