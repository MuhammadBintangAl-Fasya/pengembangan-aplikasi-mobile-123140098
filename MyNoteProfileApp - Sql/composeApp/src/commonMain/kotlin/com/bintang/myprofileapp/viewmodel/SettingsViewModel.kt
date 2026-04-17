package com.bintang.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.data.SortOrder
import kotlinx.coroutines.flow.StateFlow

/**
 * SettingsViewModel — mengelola preferences pengguna.
 *
 * Preferences yang dikelola:
 * - Theme (light / dark / system)
 * - Sort order notes
 * - Dark mode toggle
 *
 * Semua perubahan langsung tersimpan ke local storage
 * dan otomatis diterapkan ke UI via StateFlow.
 */
class SettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {

    // ─── THEME ───────────────────────────────────────────────

    val themeFlow: StateFlow<String> = settingsManager.themeFlow

    val currentTheme: String
        get() = settingsManager.theme

    fun setTheme(theme: String) {
        settingsManager.theme = theme
        // Sinkronkan isDarkMode berdasarkan tema yang dipilih
        when (theme) {
            "dark" -> settingsManager.isDarkMode = true
            "light" -> settingsManager.isDarkMode = false
            // "system" → biarkan OS yang menentukan (untuk sekarang default false)
        }
    }

    // ─── DARK MODE ───────────────────────────────────────────

    val isDarkModeFlow: StateFlow<Boolean> = settingsManager.isDarkModeFlow

    fun toggleDarkMode() {
        val newValue = !settingsManager.isDarkMode
        settingsManager.isDarkMode = newValue
        settingsManager.theme = if (newValue) "dark" else "light"
    }

    // ─── SORT ORDER ──────────────────────────────────────────

    val sortOrderFlow: StateFlow<SortOrder> = settingsManager.sortOrderFlow

    val currentSortOrder: SortOrder
        get() = settingsManager.sortOrder

    fun setSortOrder(sortOrder: SortOrder) {
        settingsManager.sortOrder = sortOrder
    }
}
