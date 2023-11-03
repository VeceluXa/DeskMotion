package com.danilovfa.deskmotion.sender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.arkivanov.decompose.defaultComponentContext
import com.danilovfa.deskmotion.sender.features.root.DefaultRootComponent
import com.danilovfa.deskmotion.sender.features.root.RootScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = DefaultRootComponent(defaultComponentContext())
        setContent {
            DeskMotionTheme(
                useDarkTheme = isSystemInDarkTheme()
            ) {
                RootScreen(root)
            }
        }
    }
}
