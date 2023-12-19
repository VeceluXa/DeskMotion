package com.danilovfa.deskmotion.receiver.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Level(
    val id: Long,
    val name: String,
    val targets: List<Coordinate>,
    val isCompleted: Boolean = false
)

val defaultLevels = listOf(
    Level(
        id = 0,
        name = "Level 1",
        targets = listOf(
            Coordinate(
                500,
                300,
                7000
            ),
            Coordinate(
                700,
                500,
                14000
            ),
            Coordinate(
                300,
                500,
                21000
            )
        ),
    ),
    Level(
        id = 1,
        name = "Level 2",
        targets = listOf(
            Coordinate(
                100,
                200,
                5000
            ),
            Coordinate(
                600,
                500,
                10000,
            ),
            Coordinate(
                250,
                100,
                15000
            ),
            Coordinate(
                100,
                450,
                20000
            )
        )
    ),
    Level(
        id = 2,
        name = "Level 3",
        targets = listOf(
            Coordinate(
                100,
                100,
                3000
            ),
            Coordinate(
                700,
                550,
                6000
            ),
            Coordinate(
                200,
                200,
                9000
            ),
            Coordinate(
                670,
                100,
                12000
            ),
            Coordinate(
                350,
                400,
                15000
            ),
            Coordinate(
                100,
                100,
                18000
            ),
            Coordinate(
                700,
                500,
                21000
            )
        )
    )
)
