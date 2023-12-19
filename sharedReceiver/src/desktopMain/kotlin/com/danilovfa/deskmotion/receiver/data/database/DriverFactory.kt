package com.danilovfa.deskmotion.receiver.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.utils.Constants
import org.apache.commons.lang3.SystemUtils
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val databasePath = when {
            SystemUtils.IS_OS_WINDOWS -> {
                File(System.getProperty("java.io.tmpdir"), "${Constants.DATABASE_NAME}.db")
            }

            SystemUtils.IS_OS_MAC -> {
                File(System.getProperty("java.io.tmpdir"), "${Constants.DATABASE_NAME}.db")
            }

            SystemUtils.IS_OS_LINUX -> {
                File(System.getProperty("java.io.tmpdir"), "${Constants.DATABASE_NAME}.db")
            }

            else -> {
                File(System.getProperty("java.io.tmpdir"), "${Constants.DATABASE_NAME}.db")
            }
        }

        val driver: SqlDriver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.path}")

        if (databasePath.exists().not()) {
            DeskMotionDatabase.Schema.create(driver)
        }

        return driver
    }
}