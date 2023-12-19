package com.danilovfa.deskmotion.ui.view.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.modifier.consumeTouches
import com.danilovfa.deskmotion.ui.modifier.rememberMinSize
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.theme.micro
import com.danilovfa.deskmotion.ui.theme.tiny
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.text.Text

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = DeskMotionButtonColors.primaryButtonColors(),
    maxLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.tiny,
    contentPadding: PaddingValues = largeButtonContentPadding,
    loading: Boolean = false,
    icon: Painter? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .consumeTouches(loading, onClick)
            .rememberMinSize { _, _ -> !loading }
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = DeskMotionTheme.colors.inversePrimary,
                    radius = 10.dp
                )
            ),
        enabled = enabled,
        elevation = null,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = colors.contentColor(enabled).value,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                    )
                    HSpacer(8.dp)
                }
                Text(
                    text = text,
                    maxLines = maxLines,
                    textAlign = TextAlign.Center,
                    style = DeskMotionTypography.textMedium18,
                )
            }
        }
    }
}

@Composable
fun PrimaryButtonLarge(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = DeskMotionButtonColors.primaryButtonColors(),
    maxLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.tiny,
    contentPadding: PaddingValues = largeButtonContentPadding,
    loading: Boolean = false,
    icon: Painter? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = largeButtonHeight)
            .consumeTouches(loading, onClick)
            .rememberMinSize { _, _ -> !loading }
            .indication(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = DeskMotionTheme.colors.inversePrimary,
                    radius = 10.dp
                )
            ),
        enabled = enabled,
        elevation = null,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = colors.contentColor(enabled).value,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                    )
                    HSpacer(8.dp)
                }
                Text(
                    text = text,
                    maxLines = maxLines,
                    textAlign = TextAlign.Center,
                    style = DeskMotionTypography.textMedium18,
                )
            }
        }
    }
}

@Composable
fun OutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = DeskMotionButtonColors.outlineButtonColors(),
    maxLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.tiny,
    contentPadding: PaddingValues = LargeButtonContentPadding,
    loading: Boolean = false,
    icon: Painter? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .consumeTouches(loading) {}
            .rememberMinSize { _, _ -> !loading },
        enabled = enabled,
        elevation = null,
        shape = shape,
        colors = colors,
        border = BorderStroke(width = 1.dp, color = colors.contentColor(enabled).value),
        contentPadding = contentPadding,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = colors.contentColor(enabled).value,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = colors.contentColor(enabled).value
                    )
                    HSpacer(8.dp)
                }
                Text(
                    text = text,
                    maxLines = maxLines,
                    textAlign = TextAlign.Center,
                    style = DeskMotionTypography.textMedium18,
                )
            }
        }
    }
}

@Composable
fun OutlineButtonLarge(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = DeskMotionButtonColors.outlineButtonColors(),
    maxLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.tiny,
    contentPadding: PaddingValues = LargeButtonContentPadding,
    loading: Boolean = false,
    icon: Painter? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = LargeButtonHeight)
            .consumeTouches(loading) {}
            .rememberMinSize { _, _ -> !loading },
        enabled = enabled,
        elevation = null,
        shape = shape,
        colors = colors,
        border = BorderStroke(width = 1.dp, color = colors.contentColor(enabled).value),
        contentPadding = contentPadding,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = colors.contentColor(enabled).value,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = colors.contentColor(enabled).value
                    )
                    HSpacer(8.dp)
                }
                Text(
                    text = text,
                    maxLines = maxLines,
                    textAlign = TextAlign.Center,
                    style = DeskMotionTypography.textMedium18,
                )
            }
        }
    }
}

@Composable
fun FloatTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Painter? = null,
    paddingValues: PaddingValues = PaddingValues(bottom = 20.dp, end = 16.dp),
) {
    TextButton(
        modifier = modifier.padding(paddingValues),
        text = text,
        onClick = onClick,
        colors = DeskMotionButtonColors.primaryButtonColors(),
        icon = icon,
        iconModifier = Modifier.size(24.dp),
        iconHorizontalSpace = 10.dp,
        shape = RoundedCornerShape(40.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        enabled = enabled,
    )
}

@Suppress("LongParameterList")
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = DeskMotionButtonColors.textButtonColors(),
    shape: Shape = MaterialTheme.shapes.micro,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    textStyle: TextStyle = DeskMotionTypography.textMedium18,
    loading: Boolean = false,
    icon: Painter? = null,
    iconModifier: Modifier? = null,
    iconHorizontalSpace: Dp = 8.dp,
) {
    androidx.compose.material.TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        elevation = null,
        shape = shape,
        colors = colors,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = colors.contentColor(enabled).value,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(contentPadding),
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = iconModifier ?: Modifier
                    )
                    HSpacer(iconHorizontalSpace)
                }
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    style = textStyle,
                )
            }
        }
    }
}

private val largeButtonHeight = 56.dp
private val largeButtonContentPadding = PaddingValues(16.dp)

object DeskMotionButtonColors {
    @Composable
    fun primaryButtonColors() = ButtonDefaults.buttonColors(
        backgroundColor = DeskMotionTheme.colors.primary,
        contentColor = DeskMotionTheme.colors.onPrimary,
        disabledBackgroundColor = DeskMotionTheme.colors.secondary,
        disabledContentColor = DeskMotionTheme.colors.onSecondary,
    )

    @Composable
    fun outlineButtonColors() = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        contentColor = DeskMotionTheme.colors.primary,
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = DeskMotionTheme.colors.secondary
    )

    @Composable
    fun textButtonColors() = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        contentColor = DeskMotionTheme.colors.primary,
        disabledBackgroundColor = Color.Transparent,
        disabledContentColor = DeskMotionTheme.colors.secondary
    )
}

private val LargeButtonHeight = 56.dp
private val LargeButtonContentPadding: PaddingValues =
    PaddingValues(vertical = 16.dp, horizontal = 16.dp)