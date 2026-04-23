package com.bintang.myprofileapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.bintang.myprofileapp.di.appModules
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(appModules)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "MyNoteApp",
    ) {
        App()
    }
}