package com.danilovfa.deskmotion.receiver

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DeskMotion"
    ) {
        App()
    }
}

@Composable
private fun App() {
    DeskMotionTheme(
        useDarkTheme = isSystemInDarkTheme()
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = DeskMotionTheme.colors.background)
        ) {

        }
    }
}