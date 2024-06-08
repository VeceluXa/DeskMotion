package com.danilovfa.deskmotion.receiver.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.utils.Constants
import com.danilovfa.deskmotion.utils.FileUtils
import org.apache.commons.lang3.SystemUtils
import java.io.File

actual class DriverFactory(
    private val fileUtils: FileUtils
) {
    actual fun createDriver(): SqlDriver {
        val dir = File(fileUtils.getFilesDirPath())
        if (dir.exists().not()) {
            dir.mkdirs()
        }

        val databasePath = File(fileUtils.getFilesDirPath(), "${Constants.DATABASE_NAME}.db")

        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.path}")

        if (databasePath.exists().not()) {
            DeskMotionDatabase.Schema.create(driver)
        }

        return driver
    }
}