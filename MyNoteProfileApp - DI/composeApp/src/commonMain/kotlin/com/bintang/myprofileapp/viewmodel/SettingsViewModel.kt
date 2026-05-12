package com.bintang.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.data.SortOrder
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val themeFlow: StateFlow<String> = settingsManager.themeFlow

    val currentTheme: String
        get() = settingsManager.theme

    fun setTheme(theme: String) {
        settingsManager.theme = theme
        when (theme) {
            "dark" -> settingsManager.isDarkMode = true
            "light" -> settingsManager.isDarkMode = false
        }
    }

    val isDarkModeFlow: StateFlow<Boolean> = settingsManager.isDarkModeFlow

    fun toggleDarkMode() {
        val newValue = !settingsManager.isDarkMode
        settingsManager.isDarkMode = newValue
        settingsManager.theme = if (newValue) "dark" else "light"
    }

    val sortOrderFlow: StateFlow<SortOrder> = settingsManager.sortOrderFlow

    val currentSortOrder: SortOrder
        get() = settingsManager.sortOrder

    fun setSortOrder(sortOrder: SortOrder) {
        settingsManager.sortOrder = sortOrder
    }
}
