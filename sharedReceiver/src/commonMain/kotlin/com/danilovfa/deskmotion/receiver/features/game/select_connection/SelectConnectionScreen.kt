package com.danilovfa.deskmotion.receiver.features.game.select_connection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.HSpacer
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GameMainScreen(component: GameSelectConnectionComponent) {
    GameMainLayout(component)
}

@Composable
private fun GameMainLayout(component: GameSelectConnectionComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Toolbar(
            title = stringResource(MR.strings.app_name),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = component::onBackClicked
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GameConfigButton(
                painter = DeskMotionIcon.WiFi,
                name = stringResource(MR.strings.wifi),
                onClick = component::onWifiConfigClicked
            )
            HSpacer(DeskMotionDimension.layoutLargeMargin)
            GameConfigButton(
                painter = DeskMotionIcon.Bluetooth,
                name = stringResource(MR.strings.bluetooth),
                onClick = component::onBluetoothConfigClicked
            )
        }
    }
}

@Composable
fun GameConfigButton(
    painter: Painter,
    name: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                color = DeskMotionTheme.colors.secondaryContainer,
                shape = RoundedCornerShape(32.dp)
            )
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(32.dp))
            .padding(24.dp)
            .size(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = name,
            tint = DeskMotionTheme.colors.primary,
            modifier = Modifier
                .size(96.dp)
        )

        VSpacer(24.dp)

        Text(
            text = name,
            textAlign = TextAlign.Center,
            style = DeskMotionTypography.titleBook34,
            color = DeskMotionTheme.colors.onSecondaryContainer,
            modifier = Modifier
        )
    }
}