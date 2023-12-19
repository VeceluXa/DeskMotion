package com.danilovfa.deskmotion.sender.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.sender.features.configuration.bluetooth.BluetoothScreen
import com.danilovfa.deskmotion.sender.features.configuration.wifi.WifiScreen
import com.danilovfa.deskmotion.sender.features.game.GameScreen
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.buttons.Button
import com.danilovfa.deskmotion.ui.view.buttons.OutlinedButton
import com.danilovfa.deskmotion.ui.view.text.Text
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MainScreen(component: MainComponent) {
    MainLayout(component)
}

@Composable
private fun MainLayout(component: MainComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Toolbar(title = stringResource(MR.strings.app_name))
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(MR.strings.start_game),
                style = DeskMotionTypography.titleBook34,
                color = DeskMotionTheme.colors.onBackground
            )
            VSpacer(DeskMotionDimension.layoutLargeMargin)
            Button(
                onClick = component::onWifiConfigClicked,
                text = stringResource(MR.strings.wifi),
                modifier = Modifier.fillMaxWidth()
            )
            VSpacer(DeskMotionDimension.layoutMainMargin)
            OutlinedButton(
                onClick = component::onBluetoothConfigClicked,
                text = stringResource(MR.strings.bluetooth),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}