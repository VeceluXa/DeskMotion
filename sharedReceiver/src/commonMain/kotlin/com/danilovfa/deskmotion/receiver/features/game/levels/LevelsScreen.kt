package com.danilovfa.deskmotion.receiver.features.game.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danilovfa.deskmotion.receiver.features.game.levels.store.LevelsStore
import com.danilovfa.deskmotion.ui.theme.DeskMotionTheme
import com.danilovfa.deskmotion.ui.view.state.LoaderLayout
import com.danilovfa.deskmotion.ui.view.toolbar.Toolbar
import dev.icerock.moko.resources.compose.stringResource
import com.danilovfa.deskmotion.MR
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.ui.theme.DeskMotionDimension
import com.danilovfa.deskmotion.ui.theme.DeskMotionTypography
import com.danilovfa.deskmotion.ui.view.VSpacer
import com.danilovfa.deskmotion.ui.view.text.Text
import com.danilovfa.deskmotion.ui.view.toolbar.NavigationIcon

@Composable
fun LevelsScreen(component: LevelsComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeskMotionTheme.colors.background)
    ) {
        Toolbar(title = stringResource(MR.strings.levels))
        LevelsLayout(component, state, Modifier.weight(1f))
    }
}

@Composable
private fun LevelsLayout(
    component: LevelsComponent,
    state: LevelsStore.State,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VSpacer(DeskMotionDimension.layoutExtraLargeMargin)

        Text(
            text = stringResource(MR.strings.select_level),
            style = DeskMotionTypography.titleBook34,
            color = DeskMotionTheme.colors.onBackground
        )

        VSpacer(DeskMotionDimension.layoutLargeMargin)

        LazyColumn {
            items(state.levels) { level ->
                LevelItem(
                    level = level,
                    onClick = { component.onEvent(LevelsStore.Intent.OnLevelClicked(level)) }
                )
            }
        }

        LoaderLayout(showLoader = state.isLoaderVisible)
    }
}

@Composable
private fun LevelItem(level: Level, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = DeskMotionDimension.layoutMediumMargin)
            .width(300.dp)
            .background(
                color = DeskMotionTheme.colors.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(12.dp))
            .padding(
                horizontal = DeskMotionDimension.layoutMainMargin,
                vertical = DeskMotionDimension.layoutMediumMargin
            )
    ) {
        Text(
            text = level.name,
            style = DeskMotionTypography.textBook24,
            color = DeskMotionTheme.colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}