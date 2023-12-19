package com.danilovfa.deskmotion.utils.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentTime() = Clock.System.now().toEpochMilliseconds()

fun formattedEpochMillis(epochMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val localTime = localDateTime.time
    return localTime.toString()
}

fun formattedDateTime(epochMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val hours = localDateTime.hour.toString().padStart(2, '0')
    val minutes = localDateTime.minute.toString().padStart(2, '0')
    val seconds = localDateTime.second.toString().padStart(2, '0')

    val year = localDateTime.year
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')

    return "$year.$month.$day $hours:$minutes:$seconds"
}