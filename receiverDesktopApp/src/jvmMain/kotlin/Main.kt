package com.danilovfa.deskmotion.receiver

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.danilovfa.deskmotion.receiver.features.root.DefaultRootComponent
import com.danilovfa.deskmotion.receiver.features.root.RootScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import java.awt.Dimension
import javax.swing.SwingUtilities
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.di.sharedReceiverModules
import com.danilovfa.deskmotion.receiver.features.root.RootComponent
import com.danilovfa.deskmotion.receiver.features.settings.root.SettingsRootComponent
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.context.startKoin
import java.util.UUID
import kotlin.system.exitProcess

fun main() {
    startKoin {
        modules(sharedReceiverModules)
    }

    val lifecycle = LifecycleRegistry()

    val rootComponent =
        runOnUiThread {
            DefaultRootComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                storeFactory = DefaultStoreFactory(),
                output = ::onRootOutput
            )
        }

    application {
        for (window in MyApplicationState.windows) {
            key(window) {
                MainWindow(rootComponent)
            }
        }
    }
}

@Composable
private fun ApplicationScope.MainWindow(rootComponent: RootComponent) {
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

private fun onRootOutput(output: RootComponent.Output) {
    when (output) {
        RootComponent.Output.Restart -> MyApplicationState.restart()
    }
}

private object MyApplicationState {
    val windows = mutableStateListOf<MyWindowState>()

    init {
        windows.add(MyWindowState())
    }

    fun openNewWindow() {
        windows.add(MyWindowState())
    }

    fun exit() {
        windows.clear()
    }

    fun restart() {
        exit()
        openNewWindow()
    }

    private fun MyWindowState(
        title: StringResource = MR.strings.app_name,
        id: Long = UUID.randomUUID().leastSignificantBits
    ) = MyWindowState(
        title = title,
        id = id,
        openNewWindow = ::openNewWindow,
        restart = ::restart,
        exit = ::exit,
        close = windows::remove
    )
}

private class MyWindowState(
    val title: StringResource,
    val id: Long,
    val openNewWindow: () -> Unit,
    val exit: () -> Unit,
    val restart: () -> Unit,
    private val close: (MyWindowState) -> Unit
) {
    fun close() = close(this)
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