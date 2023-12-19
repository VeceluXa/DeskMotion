package com.danilovfa.deskmotion.receiver

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.danilovfa.deskmotion.receiver.features.root.DefaultRootComponent
import com.danilovfa.deskmotion.receiver.features.root.RootScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.text.Text
import java.awt.Dimension
import javax.swing.SwingUtilities
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.di.sharedModules
import com.danilovfa.deskmotion.receiver.di.sharedReceiverModules
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(sharedReceiverModules)
    }

    val lifecycle = LifecycleRegistry()

    val rootComponent =
        runOnUiThread {
            DefaultRootComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                storeFactory = DefaultStoreFactory()
            )
        }

    application {
        val windowState = rememberWindowState()
        windowState.placement = WindowPlacement.Maximized
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = stringResource(MR.strings.app_name),
            icon = painterResource("icon.png")
        ) {
            window.minimumSize = Dimension(800, 600)

            DeskMotionTheme(
                useDarkTheme = isSystemInDarkTheme()
            ) {
                RootScreen(rootComponent)
            }
        }
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}