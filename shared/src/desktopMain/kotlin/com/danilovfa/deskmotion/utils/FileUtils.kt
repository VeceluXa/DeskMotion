package com.danilovfa.deskmotion.utils

import com.danilovfa.deskmotion.utils.Constants.APP_NAME
import org.apache.commons.lang3.SystemUtils

actual class FileUtils {
    actual fun getFilesDirPath(): String {
        val home = System.getProperty("user.home")
        return when {
            SystemUtils.IS_OS_LINUX -> {
                "$home\\.desk_motion"
            }
            SystemUtils.IS_OS_MAC -> {
                "$home\\Library\\Application Support\\$APP_NAME"
            }
            SystemUtils.IS_OS_WINDOWS -> {
                "$home\\AppData\\Local\\$APP_NAME"
            }
            else -> {
                TODO("Not implemented")
            }
        }
    }
}