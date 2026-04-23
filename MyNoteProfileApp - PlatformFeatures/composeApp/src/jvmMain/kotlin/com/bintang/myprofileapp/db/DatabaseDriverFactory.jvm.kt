package com.bintang.myprofileapp.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:notes.db")
        try {
            NotesDatabase.Schema.create(driver)
        } catch (_: Exception) {
        }
        return driver
    }
}
