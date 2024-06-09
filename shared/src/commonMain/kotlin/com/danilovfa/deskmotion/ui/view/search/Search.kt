package com.danilovfa.deskmotion.ui.view.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.theme.Shapes
import com.danilovfa.deskmotion.ui.theme.tiny
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.animation.IconAnimatedVisibility
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon
import com.danilovfa.deskmotion.ui.view.text.Text

@Composable
fun Search(
    value: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    errorText: String = "",
    onValueResetClick: () -> Unit = {},
    enabled: Boolean = true,
    isError: Boolean = false,
    textStyle: TextStyle = DeskMotionTypography.textBook18,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(getBorderlineColor(isError, enabled))

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(DeskMotionTheme.colors.secondaryContainer, shape = Shapes.tiny)
                .border(BorderStroke(width = 1.dp, color = borderColor), shape = Shapes.tiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                painter = DeskMotionIcon.Search,
                tint = DeskMotionTheme.colors.onSecondaryContainer,
                contentDescription = null,
            )
            SearchTextField(
                value = value,
                placeholderText = placeholderText,
                textStyle = textStyle,
                enabled = enabled,
                onValueChange = onValueChange,
                onValueResetClick = onValueResetClick,
                isError = isError,
                interactionSource = interactionSource,
            )
        }

        ErrorHint(errorText, isError)
    }
}

@Composable
private fun SearchTextField(
    value: String,
    placeholderText: String,
    enabled: Boolean,
    textStyle: TextStyle,
    onValueChange: (String) -> Unit,
    onValueResetClick: () -> Unit = {},
    singleLine: Boolean = true,
    isError: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    val onTextFieldValueChange: (TextFieldValue) -> Unit = { fieldValue ->
        textFieldValueState = fieldValue
        if (value != fieldValue.text) {
            onValueChange(fieldValue.text)
        }
    }

    val colors = createSearchTextFieldColors()
    val textColor = textStyle.color.takeOrElse { colors.textColor(enabled).value }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        value = textFieldValue,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        onValueChange = onTextFieldValueChange,
        enabled = enabled,
        readOnly = false,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default,
        keyboardActions = KeyboardActions(),
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = 1,
        minLines = 1,
        decorationBox = @Composable { innerTextField ->
            SearchFieldDecorationBox(
                textFieldValue = textFieldValue,
                innerTextField = innerTextField,
                placeholderText = placeholderText,
                onValueResetClick = onValueResetClick,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun SearchFieldDecorationBox(
    textFieldValue: TextFieldValue,
    innerTextField: @Composable () -> Unit,
    placeholderText: String,
    onValueResetClick: () -> Unit,
    singleLine: Boolean,
    enabled: Boolean,
    isError: Boolean,
    interactionSource: MutableInteractionSource,
    colors: TextFieldColors
) {
    TextFieldDefaults.TextFieldDecorationBox(
        value = textFieldValue.text,
        visualTransformation = VisualTransformation.None,
        innerTextField = innerTextField,
        placeholder = { Text(placeholderText) },
        label = null,
        leadingIcon = null,
        trailingIcon = {
            IconAnimatedVisibility(visible = textFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = onValueResetClick,
                    modifier = Modifier.size(DeskMotionDimension.minTouchSize)
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = DeskMotionIcon.Close,
                        tint = DeskMotionTheme.colors.onSecondaryContainer,
                        contentDescription = null,
                    )
                }
            }
        },
        singleLine = singleLine,
        enabled = enabled,
        isError = isError,
        interactionSource = interactionSource,
        colors = colors,
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 0.dp)
    )
}

@Composable
private fun ColumnScope.ErrorHint(
    errorText: String = "",
    isError: Boolean = false,
) {
    AnimatedVisibility(isError) {
        Column {
            VSpacer(4.dp)
            Text(
                text = errorText,
                style = DeskMotionTypography.captionBook14,
                color = DeskMotionTheme.colors.error,
            )
        }
    }
}

@Composable
private fun getBorderlineColor(
    error: Boolean,
    enabled: Boolean,
): Color {
    return when {
        error -> DeskMotionTheme.colors.error
        enabled -> Color.Transparent
        else -> Color.Transparent
    }
}

@Composable
private fun createSearchTextFieldColors(): TextFieldColors = TextFieldDefaults.textFieldColors(
    textColor = DeskMotionTheme.colors.onBackground,
    disabledTextColor = DeskMotionTheme.colors.onBackground,
    backgroundColor = Color.Transparent,
    cursorColor = DeskMotionTheme.colors.primary,
    trailingIconColor = DeskMotionTheme.colors.onBackground,
    disabledTrailingIconColor = DeskMotionTheme.colors.primary.copy(alpha = 0.6f),
    errorTrailingIconColor = DeskMotionTheme.colors.onBackground,
    placeholderColor = DeskMotionTheme.colors.onSecondaryContainer,
    disabledPlaceholderColor = DeskMotionTheme.colors.onBackground.copy(alpha = 0.6f),
)