package com.danilovfa.deskmotion.receiver.data.database.mapper

import com.danilovfa.deskmotion.receiver.domain.model.User
import deskMotionDatabase.UserEntity

fun UserEntity.toUser() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    dateOfBirth = dateOfBirth
)