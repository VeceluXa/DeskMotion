package com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart

import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import com.danilovfa.deskmotion.receiver.utils.Constants
import kotlin.math.atan2
import kotlin.math.sqrt

data class PolarCoordinate(
    val angleInRadians: Double,
    val radius: Double
)

fun Coordinate.toPolarCoordinate(): PolarCoordinate {
    val centeredX = (x - Constants.GAME_SCREEN_WIDTH / 2).toDouble()
    val centeredY = (y - Constants.GAME_SCREEN_HEIGHT / 2).toDouble()

    val radius = sqrt(centeredX * centeredX + centeredY * centeredY)
    val angleInRadians = atan2(centeredY, centeredX)

//    val positiveAngle = if (angleInRadians < 0) angleInRadians + (2 * Math.PI) else angleInRadians

    return PolarCoordinate(
        angleInRadians = angleInRadians,
        radius = radius
    )
}