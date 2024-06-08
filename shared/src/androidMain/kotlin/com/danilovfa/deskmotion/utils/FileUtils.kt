package com.danilovfa.deskmotion.utils

import android.content.Context

actual class FileUtils(
    private val context: Context
) {
    actual fun getFilesDirPath(): String {
        return context.cacheDir.absolutePath
    }
}