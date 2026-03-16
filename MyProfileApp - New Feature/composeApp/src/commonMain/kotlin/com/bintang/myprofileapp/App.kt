package com.bintang.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bintang.myprofileapp.ui.ProfileScreen
import com.bintang.myprofileapp.ui.theme.AppTheme
import com.bintang.myprofileapp.viewmodel.ProfileViewModel

@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    AppTheme(isDarkMode = uiState.isDarkMode) {
        ProfileScreen(
            uiState = uiState,
            onToggleDarkMode = { viewModel.toggleDarkMode() },
            onToggleEditing = { viewModel.toggleEditing() },
            onUpdateProfile = { name, bio -> viewModel.updateProfile(name, bio) }
        )
    }
}
