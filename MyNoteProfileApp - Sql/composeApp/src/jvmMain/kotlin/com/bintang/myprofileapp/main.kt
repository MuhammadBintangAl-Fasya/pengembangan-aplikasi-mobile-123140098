package com.bintang.myprofileapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.bintang.myprofileapp.db.DatabaseDriverFactory

fun main() = application {
    // Buat DatabaseDriverFactory untuk JVM Desktop
    val driverFactory = DatabaseDriverFactory()

    Window(
        onCloseRequest = ::exitApplication,
        title = "MyProfileApp",
    ) {
        App(driverFactory = driverFactory)
    }
}