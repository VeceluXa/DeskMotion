package com.danilovfa.deskmotion.receiver.features.game.configuration.wifi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.features.game.configuration.wifi.store.WifiStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.buttons.PrimaryButtonLarge
import com.danilovfa.deskmotion.ui.view.text.LargeTextField
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon

@Composable
fun WifiScreen(component: WifiComponent) {
    val state by component.state.collectAsState()
    WifiLayout(component = component, state = state)
}

@Composable
private fun WifiLayout(component: WifiComponent, state: WifiStore.State) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar(
            title = stringResource(MR.strings.wifi),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = { component.onEvent(WifiStore.Intent.OnBackClicked) }
        )
        WifiConfig(component = component, state = state)
    }

}

@Composable
private fun WifiConfig(component: WifiComponent, state: WifiStore.State) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.4f)
    ) {
        VSpacer(DeskMotionDimension.layoutLargeMargin)
        LargeTextField(
            value = state.ip,
            onValueChange = { component.onEvent(WifiStore.Intent.OnIpChanged(it)) },
            labelText = stringResource(MR.strings.ip),
            isError = state.isIpError,
            isRequired = true
        )
        VSpacer(DeskMotionDimension.layoutMainMargin)
        LargeTextField(
            value = state.port,
            onValueChange = { component.onEvent(WifiStore.Intent.OnPortChanged(it)) },
            labelText = stringResource(MR.strings.port),
            isError = state.isPortError,
            isRequired = true
        )

        state.localIpAddress?.let { ipAddress ->
            VSpacer(DeskMotionDimension.layoutMainMargin)
            Text(
                text = stringResource(MR.strings.your_ip_address, ipAddress),
                color = DeskMotionTheme.colors.onBackground,
                modifier = Modifier.padding(DeskMotionDimension.layoutHorizontalMargin)
            )
        }


        VSpacer(DeskMotionDimension.layoutLargeMargin)
        PrimaryButtonLarge(
            text = stringResource(MR.strings.start),
            onClick = { component.onEvent(WifiStore.Intent.OnStartClicked) },
            enabled = state.isButtonEnabled,
            modifier = Modifier.padding(DeskMotionDimension.layoutHorizontalMargin)
        )
    }
}