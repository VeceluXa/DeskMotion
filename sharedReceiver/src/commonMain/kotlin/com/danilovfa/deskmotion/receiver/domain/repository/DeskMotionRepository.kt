package com.danilovfa.deskmotion.receiver.domain.repository

import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import kotlinx.coroutines.flow.Flow

interface DeskMotionRepository {
    suspend fun getLevels(): List<Level>
    suspend fun getLevel(id: Long): Level
    suspend fun addLevel(level: Level)
    suspend fun addLog(log: PlayLog)
    fun getLogs(): Flow<List<PlayLog>>
    suspend fun getLog(id: Long): PlayLog
}