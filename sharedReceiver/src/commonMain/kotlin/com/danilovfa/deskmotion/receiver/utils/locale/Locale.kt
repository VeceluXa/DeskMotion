package com.danilovfa.deskmotion.receiver.utils.locale

import co.touchlab.kermit.Logger
import dev.icerock.moko.resources.desc.StringDesc

fun setLocale(locale: DeskMotionLocale) {
    StringDesc.localeType = StringDesc.LocaleType.Custom(locale.code)

    StringDesc.localeType
}

fun DeskMotionLocale.isCurrent(): Boolean {
    val code = this.code
    val currentLocale = StringDesc.localeType.currentLocale
    Logger.d("DeskMotionLocale") {
        "DataStore: $code, Current: ${currentLocale.language}"
    }

    return code in currentLocale.language
}

enum class DeskMotionLocale(val code: String, val localeName: String) {
    ENGLISH(code = "en", localeName = "English"),
    RUSSIAN(code = "ru", localeName = "Русский")
}