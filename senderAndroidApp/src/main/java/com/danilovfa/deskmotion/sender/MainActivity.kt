package com.danilovfa.deskmotion.sender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeskMotionTheme(
                useDarkTheme = isSystemInDarkTheme()
            ) {

            }
        }
    }
}
