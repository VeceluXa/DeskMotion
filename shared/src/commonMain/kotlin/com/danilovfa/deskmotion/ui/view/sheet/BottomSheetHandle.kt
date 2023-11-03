package com.danilovfa.deskmotion.ui.view.sheet

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.textShimmer

@Composable
fun BottomSheetHandle(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier
            .width(48.dp)
            .clip(MaterialTheme.shapes.textShimmer),
        thickness = 4.dp,
        color = DeskMotionTheme.colors.onSurface
    )
}