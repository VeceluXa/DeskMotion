package com.danilovfa.deskmotion.receiver.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.danilovfa.deskmotion.receiver.data.database.mapper.toLevel
import com.danilovfa.deskmotion.receiver.data.database.mapper.toPlayLog
import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.domain.model.Level
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class DeskMotionRepositoryImpl(
    private val database: DeskMotionDatabase,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : DeskMotionRepository {

    private val levelQueries = database.levelQueries
    private val playLogQueries = database.playLogQueries

    override suspend fun getLevels(): List<Level> = withContext(ioDispatcher) {
        return@withContext levelQueries
            .getLevels()
            .executeAsList()
            .map { levelEntity ->
                levelEntity.toLevel()
            }
    }

    override suspend fun getLevel(id: Long): Level = withContext(ioDispatcher) {
        return@withContext levelQueries
            .getLevelById(id)
            .executeAsOne()
            .toLevel()
    }

    override suspend fun addLevel(level: Level) = withContext (ioDispatcher) {
        levelQueries.insertLevel(
            level.id,
            level.name,
            level.targets,
            level.isCompleted
        )
    }

    override suspend fun addLog(log: PlayLog) = withContext(ioDispatcher) {
        playLogQueries.insertPlayLog(
            id = log.id,
            userId = log.userId,
            levelId = log.levelId,
            log = log.log,
            score = log.score.toLong(),
            completedEpochMillis = log.completedEpochMillis
        )
    }

    override fun getLogs(): Flow<List<PlayLog>> {
        return playLogQueries
            .getPlayLogs()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map { playLogEntity ->
                    playLogEntity.toPlayLog()
                }
            }
    }

    override suspend fun getLog(id: Long): PlayLog = withContext(ioDispatcher) {
        return@withContext playLogQueries
            .getPlayLogById(id)
            .executeAsOne()
            .toPlayLog()
    }

}