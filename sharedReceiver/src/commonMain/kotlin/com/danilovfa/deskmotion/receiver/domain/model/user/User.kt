package com.danilovfa.deskmotion.receiver.domain.model.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val dateOfBirth: LocalDate
)