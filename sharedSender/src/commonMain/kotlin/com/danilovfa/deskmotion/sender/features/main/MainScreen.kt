package com.danilovfa.deskmotion.sender.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.theme.Dimension

@Composable
fun MainScreen(component: MainComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(color = DeskMotionTheme.colors.primaryContainer, shape = RoundedCornerShape(16.dp)),
        ) {
            Text(
                text = "Test Text 123 SADASD",
                style = DeskMotionTypography.titleBook34,
                color = DeskMotionTheme.colors.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}