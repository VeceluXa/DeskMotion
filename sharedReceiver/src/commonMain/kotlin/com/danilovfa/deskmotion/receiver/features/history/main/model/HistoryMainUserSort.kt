package com.danilovfa.deskmotion.receiver.features.history.main.model

import com.danilovfa.deskmotion.MR
import dev.icerock.moko.resources.StringResource

sealed class HistoryMainUserSort {
    abstract val isDescending: Boolean
    abstract val titleRes: StringResource

    abstract fun copySort(isDescending: Boolean): HistoryMainUserSort

    data class FirstName(
        override val isDescending: Boolean = true,
        override val titleRes: StringResource = MR.strings.first_name
    ) : HistoryMainUserSort() {
        override fun copySort(isDescending: Boolean) = copy(isDescending = isDescending)
    }

    data class LastName(
        override val isDescending: Boolean = true,
        override val titleRes: StringResource = MR.strings.last_name
    ) : HistoryMainUserSort() {
        override fun copySort(isDescending: Boolean) = copy(isDescending = isDescending)
    }

    data class MiddleName(
        override val isDescending: Boolean = true,
        override val titleRes: StringResource = MR.strings.middle_name
    ) : HistoryMainUserSort() {
        override fun copySort(isDescending: Boolean) = copy(isDescending = isDescending)
    }

    data class DateOfBirth(
        override val isDescending: Boolean = true,
        override val titleRes: StringResource = MR.strings.date_of_birth
    ) : HistoryMainUserSort() {
        override fun copySort(isDescending: Boolean) = copy(isDescending = isDescending)
    }
}