package com.bintang.myprofileapp.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsManager(private val settings: Settings) {

    companion object {
        private const val KEY_THEME = "app_theme"
        private const val KEY_SORT_ORDER = "sort_order"
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }

    private val _themeFlow = MutableStateFlow(theme)
    val themeFlow: StateFlow<String> = _themeFlow.asStateFlow()

    var theme: String
        get() = settings[KEY_THEME, "system"]
        set(value) {
            settings[KEY_THEME] = value
            _themeFlow.value = value
        }

    private val _isDarkModeFlow = MutableStateFlow(isDarkMode)
    val isDarkModeFlow: StateFlow<Boolean> = _isDarkModeFlow.asStateFlow()

    var isDarkMode: Boolean
        get() = settings[KEY_IS_DARK_MODE, false]
        set(value) {
            settings[KEY_IS_DARK_MODE] = value
            _isDarkModeFlow.value = value
        }

    private val _sortOrderFlow = MutableStateFlow(sortOrder)
    val sortOrderFlow: StateFlow<SortOrder> = _sortOrderFlow.asStateFlow()

    var sortOrder: SortOrder
        get() {
            val name = settings[KEY_SORT_ORDER, SortOrder.UPDATED_DESC.name]
            return try {
                SortOrder.valueOf(name)
            } catch (_: Exception) {
                SortOrder.UPDATED_DESC
            }
        }
        set(value) {
            settings[KEY_SORT_ORDER] = value.name
            _sortOrderFlow.value = value
        }
}
