package com.danilovfa.deskmotion.sender.features.configuration.bluetooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.view.stub.EmptyStub
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon

@Composable
fun BluetoothScreen(component: BluetoothComponent) {
    BluetoothLayout(component)
}

@Composable
private fun BluetoothLayout(component: BluetoothComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(
            title = stringResource(MR.strings.bluetooth),
            navigationIcon = NavigationIcon.Back,
            onNavigationClick = component::onBackClicked
        )
        EmptyStub(
            title = stringResource(MR.strings.feature_stub_title), 
            message = stringResource(MR.strings.feature_stub_description),
            modifier = Modifier.padding(DeskMotionDimension.layoutLargeMargin)
        )
    }
}