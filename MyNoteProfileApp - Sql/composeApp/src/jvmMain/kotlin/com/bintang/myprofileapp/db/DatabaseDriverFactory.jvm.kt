package com.bintang.myprofileapp.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

/**
 * JVM Desktop implementation — menggunakan JdbcSqliteDriver
 * yang menyimpan database sebagai file lokal "notes.db".
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:notes.db")
        try {
            NotesDatabase.Schema.create(driver)
        } catch (_: Exception) {
            // Schema sudah ada — terjadi ketika aplikasi bukan pertama kali dijalankan
        }
        return driver
    }
}
