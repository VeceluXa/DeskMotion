package com.danilovfa.deskmotion.receiver.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayLog(
    val id: Long,
    val levelId: Long,
    val log: List<Coordinate>,
    val score: Int,
    val completedEpochMillis: Long
)