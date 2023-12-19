package com.danilovfa.deskmotion.receiver.features.root

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.StringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.images.DeskMotionIcon
import dev.icerock.moko.resources.compose.stringResource

val defaultNavigationItems = listOf(
    NavigationItemData(
        config = DefaultRootComponent.Config.Game,
        nameResource = MR.strings.game,
        painter = { DeskMotionIcon.Play },
        isActive = true
    ),
    NavigationItemData(
        config = DefaultRootComponent.Config.History,
        nameResource = MR.strings.history,
        painter = { DeskMotionIcon.History },
        isActive = false
    )
)

data class NavigationItemData(
    val config: DefaultRootComponent.Config,
    val nameResource: StringResource,
    val painter: @Composable () -> Painter,
    val isActive: Boolean
)

@Composable
fun NavigationItem(
    navigationItem: NavigationItemData,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onItemClick)
            .padding(
                horizontal = DeskMotionDimension.layoutMediumMargin,
                vertical = DeskMotionDimension.layoutMainMargin
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconContainerColor = if (navigationItem.isActive) DeskMotionTheme.colors.secondary else Color.Transparent
        val iconTint = if (navigationItem.isActive) DeskMotionTheme.colors.onSecondary else DeskMotionTheme.colors.onSecondaryContainer
        val textColor = if (navigationItem.isActive)
            DeskMotionTheme.colors.onSecondaryContainer
        else
            DeskMotionTheme.colors.onSecondaryContainer.copy(alpha = 0.8f)

        Icon(
            painter = navigationItem.painter(),
            tint = iconTint,
            contentDescription = stringResource(navigationItem.nameResource),
            modifier = Modifier
                .background(
                    color = iconContainerColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .size(48.dp)
                .padding(8.dp)
        )

        VSpacer(DeskMotionDimension.layoutSmallMargin)

        Text(
            text = stringResource(navigationItem.nameResource),
            color = textColor,
            style = DeskMotionTypography.captionMedium12
        )
    }
}