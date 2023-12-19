package com.danilovfa.deskmotion.ui.view.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.animation.AnimatedVisibilityNullableValue
import com.danilovfa.deskmotion.ui.view.animation.IconAnimatedVisibility
import com.danilovfa.deskmotion.ui.view.buttons.TextButton
import com.danilovfa.deskmotion.ui.view.text.Text

@Composable
fun AlertDialog(
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    isVisible: Boolean = false,
    text: String? = null,
    title: String? = null,
    dismissButtonText: String? = null,
    confirmButtonText: String? = null,
) {
    IconAnimatedVisibility(isVisible) {

        androidx.compose.material.AlertDialog(
            modifier = Modifier.shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp)),
            shape = RoundedCornerShape(2.dp),
            backgroundColor = DeskMotionTheme.colors.surface,
            contentColor = DeskMotionTheme.colors.onSurface,
            onDismissRequest = onDismissClick,
            title = if (!title.isNullOrEmpty()) {
                {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        style = DeskMotionTypography.textMedium18,
                    )
                }
            } else null,
            text = if (!text.isNullOrBlank()) {
                {
                    Text(
                        text = text,
                        color = DeskMotionTheme.colors.inverseSurface,
                        style = DeskMotionTypography.captionBook16,
                    )
                }
            } else null,
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(start = 12.dp, end = 12.dp, bottom = 2.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (!dismissButtonText.isNullOrEmpty()) {
                        TextButton(
                            onClick = onDismissClick,
                            text = dismissButtonText.uppercase(),
                            textStyle = DeskMotionTypography.captionMedium14,
                        )
                    }
                    if (!confirmButtonText.isNullOrEmpty()) {
                        HSpacer(16.dp)
                        TextButton(
                            onClick = onConfirmClick,
                            text = confirmButtonText.uppercase(),
                            textStyle = DeskMotionTypography.captionMedium14,
                        )
                    }
                }
            },
        )
    }
}