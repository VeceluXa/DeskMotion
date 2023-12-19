package com.danilovfa.deskmotion.ui.view.stub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.buttons.OutlineButtonLarge
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIllustration
import com.danilovfa.deskmotion.ui.view.text.Text

@Composable
fun EmptyStub(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    image: Painter = DeskMotionIllustration.Empty,
    buttonTitle: String? = null,
    onButtonClick: () -> Unit = {},
    textColor: Color = DeskMotionTheme.colors.onBackground,
    fillMaxSize: Boolean = true,
) = BaseStub(
    modifier = modifier,
    title = title,
    message = message,
    image = image,
    buttonTitle = buttonTitle,
    onButtonClick = onButtonClick,
    textColor = textColor,
    fillMaxSize = fillMaxSize,
)

@Composable
fun BaseStub(
    title: String,
    message: String,
    image: Painter,
    buttonTitle: String?,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = DeskMotionTheme.colors.onBackground,
    fillMaxSize: Boolean = true,
) {
    Column(modifier = modifier) {
        val innerModifier = Modifier
            .padding(DeskMotionDimension.layoutMainMargin)
            .align(Alignment.CenterHorizontally)
            .run {
                if (fillMaxSize) {
                    this
                        .fillMaxSize()
                        .weight(1f)
                } else {
                    this
                }
            }
        Column(
            modifier = innerModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = image,
                contentDescription = null
            )
            Text(
                text = title,
                style = DeskMotionTypography.titleBook28,
                modifier = Modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center,
                color = textColor,
            )
            Text(
                text = message,
                style = DeskMotionTypography.textBook18,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center,
                color = textColor
            )
        }
        if (!buttonTitle.isNullOrBlank()) {
            OutlineButtonLarge(
                text = buttonTitle,
                onClick = onButtonClick,
                modifier = Modifier.padding(DeskMotionDimension.layoutMainMargin),
            )
        }
    }
}