package com.danilovfa.deskmotion.sender

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.danilovfa.deskmotion.sender.features.root.DefaultRootComponent
import com.danilovfa.deskmotion.sender.features.root.RootScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val componentContext = defaultComponentContext()
        val storeFactory = DefaultStoreFactory()
        val root = DefaultRootComponent(componentContext, storeFactory)
        setContent {
            DeskMotionTheme(
                useDarkTheme = isSystemInDarkTheme()
            ) {
                RootScreen(root)
            }
        }
    }
}
