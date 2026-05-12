package com.bintang.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.bintang.myprofileapp.navigation.MainScreen
import com.bintang.myprofileapp.ui.theme.AppTheme
import com.bintang.myprofileapp.viewmodel.SettingsViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsViewModel: SettingsViewModel = koinInject()
    val isDarkMode by settingsViewModel.isDarkModeFlow.collectAsState()

    AppTheme(isDarkMode = isDarkMode) {
        MainScreen()
    }
}
