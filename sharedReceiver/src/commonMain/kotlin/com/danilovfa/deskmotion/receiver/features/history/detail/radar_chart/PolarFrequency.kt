package com.danilovfa.deskmotion.receiver.features.history.detail.radar_chart

import co.touchlab.kermit.Logger
import kotlin.math.roundToInt

data class PolarFrequency(
    val angleRange: IntRange,
    val frequency: Double = 0.0
)

fun generateRadarChartData(coordinates: List<PolarCoordinate>): List<PolarFrequency> {
    var frequencies = List(8) { index ->
        PolarFrequency(angleRange = index * 45 until (index +1) * 45)
    }

    var maxFrequency = 0.0

    coordinates.forEach { coordinate ->
        val angleInDegrees = Math.toDegrees(coordinate.angleInRadians).roundToInt()

        frequencies = frequencies.map { frequency ->
            if (angleInDegrees in frequency.angleRange) {
                val newFrequency = frequency.copy(frequency = frequency.frequency + 1.0)

                if (newFrequency.frequency > maxFrequency) {
                    maxFrequency = newFrequency.frequency
                }

                newFrequency
            } else {
                frequency
            }
        }
    }

    frequencies = frequencies.map { it.copy(frequency = it.frequency / maxFrequency) }
    Logger.i { frequencies.joinToString() }

    frequencies = frequencies.reversed()
    frequencies = frequencies.subList(5, 8) + frequencies.subList(0, 5)

    return frequencies
}

fun getMaxFrequency(coordinates: List<PolarCoordinate>) {

}