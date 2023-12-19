package com.danilovfa.deskmotion.receiver.domain.model

import com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart.PolarCoordinate
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_HEIGHT
import com.danilovfa.deskmotion.receiver.utils.Constants.GAME_SCREEN_WIDTH
import kotlinx.serialization.Serializable
import kotlin.math.atan2
import kotlin.math.sqrt

@Serializable
data class Coordinate(
    val x: Int,
    val y: Int,
    val millis: Long
) {
    fun toGameScreenCoordinate(screenWidth: Int, screenHeight: Int) =
        Coordinate(
            x = x * screenWidth / GAME_SCREEN_WIDTH,
            y = y * screenHeight / GAME_SCREEN_HEIGHT,
            millis = millis
        )
}
