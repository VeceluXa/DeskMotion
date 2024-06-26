package com.danilovfa.deskmotion.receiver.domain.repository

import com.danilovfa.deskmotion.receiver.domain.model.User

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(id: Long)
    suspend fun getUser(id: Long): User?
    suspend fun getAllUsers(): List<User>
}