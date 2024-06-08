package com.danilovfa.deskmotion.receiver.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate

internal val localDateAdapter = object : ColumnAdapter<LocalDate, Long> {
    override fun decode(databaseValue: Long): LocalDate =
        LocalDate.fromEpochDays(databaseValue.toInt())

    override fun encode(value: LocalDate): Long = value.toEpochDays().toLong()
}