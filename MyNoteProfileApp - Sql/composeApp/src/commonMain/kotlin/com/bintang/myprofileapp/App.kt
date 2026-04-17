package com.bintang.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bintang.myprofileapp.data.NoteRepository
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.db.DatabaseDriverFactory
import com.bintang.myprofileapp.db.NotesDatabase
import com.bintang.myprofileapp.navigation.MainScreen
import com.bintang.myprofileapp.ui.theme.AppTheme
import com.bintang.myprofileapp.viewmodel.NotesViewModel
import com.bintang.myprofileapp.viewmodel.ProfileViewModel
import com.bintang.myprofileapp.viewmodel.SettingsViewModel
import com.russhwolf.settings.Settings

/**
 * Root Composable — entry point dari seluruh UI.
 *
 * Menerima DatabaseDriverFactory dari platform-specific code (Android/JVM)
 * untuk membuat database connection yang sesuai dengan platform.
 */
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    // ─── DEPENDENCY SETUP ─────────────────────────────────────
    // Singleton instances — hanya dibuat sekali selama lifecycle aplikasi

    val database = remember {
        NotesDatabase(driverFactory.createDriver())
    }

    val noteRepository = remember {
        NoteRepository(database)
    }

    val settingsManager = remember {
        SettingsManager(Settings())
    }

    // ─── VIEWMODELS ───────────────────────────────────────────

    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val profileUiState by profileViewModel.uiState.collectAsState()

    val notesViewModel: NotesViewModel = viewModel {
        NotesViewModel(noteRepository, settingsManager)
    }

    val settingsViewModel: SettingsViewModel = viewModel {
        SettingsViewModel(settingsManager)
    }

    // Gunakan isDarkMode dari SettingsManager (persisted)
    val isDarkMode by settingsViewModel.isDarkModeFlow.collectAsState()

    // ─── UI ───────────────────────────────────────────────────

    AppTheme(isDarkMode = isDarkMode) {
        MainScreen(
            profileUiState = profileUiState,
            onToggleDarkMode = { settingsViewModel.toggleDarkMode() },
            onToggleEditing = { profileViewModel.toggleEditing() },
            onUpdateProfile = { name, bio -> profileViewModel.updateProfile(name, bio) },
            notesViewModel = notesViewModel,
            settingsViewModel = settingsViewModel
        )
    }
}
