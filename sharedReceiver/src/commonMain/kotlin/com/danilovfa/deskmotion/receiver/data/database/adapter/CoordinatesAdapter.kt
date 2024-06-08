package com.danilovfa.deskmotion.receiver.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import com.danilovfa.deskmotion.receiver.domain.model.Coordinate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal val listOfCoordinatesAdapter = object : ColumnAdapter<List<Coordinate>, String> {
    override fun decode(databaseValue: String): List<Coordinate> {
        return Json.decodeFromString<List<Coordinate>>(databaseValue)
    }

    override fun encode(value: List<Coordinate>): String {
        return Json.encodeToString<List<Coordinate>>(value)
    }
}