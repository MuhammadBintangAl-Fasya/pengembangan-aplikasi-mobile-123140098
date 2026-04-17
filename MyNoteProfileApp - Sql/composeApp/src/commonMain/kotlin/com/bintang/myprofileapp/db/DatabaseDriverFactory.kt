package com.bintang.myprofileapp.db

import app.cash.sqldelight.db.SqlDriver

/**
 * expect class untuk membuat SqlDriver per platform.
 * Setiap platform (Android, JVM) harus menyediakan implementasi actual-nya.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
