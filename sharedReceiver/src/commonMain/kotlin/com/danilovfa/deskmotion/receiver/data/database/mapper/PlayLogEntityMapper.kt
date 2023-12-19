package com.danilovfa.deskmotion.receiver.data.database.mapper

import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import deskMotionDatabase.PlayLogEntity

fun PlayLogEntity.toPlayLog() = PlayLog(
    id = id,
    levelId = levelId,
    log = log,
    score = score.toInt(),
    completedEpochMillis = completedEpochMillis
)