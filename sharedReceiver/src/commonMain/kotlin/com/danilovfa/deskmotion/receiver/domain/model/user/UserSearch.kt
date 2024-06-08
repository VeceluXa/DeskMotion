package com.danilovfa.deskmotion.receiver.domain.model.user

import kotlinx.datetime.LocalDate

sealed class UserSearch {
    data class Name(val name: String) : UserSearch()
    data class DateOfBirth(val dateOfBirth: LocalDate) : UserSearch()
}