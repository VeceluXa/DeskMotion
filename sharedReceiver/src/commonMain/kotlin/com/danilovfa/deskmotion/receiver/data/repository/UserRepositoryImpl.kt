package com.danilovfa.deskmotion.receiver.data.repository

import co.touchlab.kermit.Logger
import com.danilovfa.deskmotion.receiver.data.database.mapper.toUser
import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.domain.model.user.User
import com.danilovfa.deskmotion.receiver.domain.repository.UserRepository
import com.danilovfa.deskmotion.receiver.utils.Constants.SETTINGS_USER_ID
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl(
    private val database: DeskMotionDatabase,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : UserRepository {
    private val userQueries = database.userQueries
    private val settings = Settings()

    override suspend fun addUser(user: User) = withContext(ioDispatcher) {
        userQueries.insertUser(
            id = null,
            firstName = user.firstName,
            middleName = user.middleName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth
        )

        val userId = userQueries.lastInsertedUserId().executeAsOne()

        Logger.d { "CreatedUserId=$userId" }

        settings[SETTINGS_USER_ID] = userId
    }

    override suspend fun updateUser(user: User) = withContext(ioDispatcher) {
        userQueries.insertUser(
            id = user.id,
            firstName = user.firstName,
            middleName = user.middleName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth
        )
    }

    override suspend fun deleteUser(id: Long) = withContext(ioDispatcher) {
        userQueries.deleteUser(id)
    }

    override suspend fun getUser(id: Long): User? = withContext(ioDispatcher) {
        return@withContext userQueries.getUser(id).executeAsOneOrNull()?.toUser()
    }
}