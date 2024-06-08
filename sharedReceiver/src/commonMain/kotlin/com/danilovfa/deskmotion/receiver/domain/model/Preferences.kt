package com.danilovfa.deskmotion.receiver.domain.model

import com.danilovfa.deskmotion.receiver.utils.locale.DeskMotionLocale
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val locale: DeskMotionLocale = DeskMotionLocale.ENGLISH
)
