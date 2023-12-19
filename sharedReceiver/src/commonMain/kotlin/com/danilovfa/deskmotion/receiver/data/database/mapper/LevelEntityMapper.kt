package com.danilovfa.deskmotion.receiver.data.database.mapper

import com.danilovfa.deskmotion.receiver.domain.model.Level
import deskMotionDatabase.LevelEntity

fun LevelEntity.toLevel() = Level(
    id = id,
    name = name,
    targets = targets,
    isCompleted = isCompleted
)