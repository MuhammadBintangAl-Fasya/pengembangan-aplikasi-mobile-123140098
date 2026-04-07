package com.bintang.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bintang.myprofileapp.navigation.MainScreen
import com.bintang.myprofileapp.ui.theme.AppTheme
import com.bintang.myprofileapp.viewmodel.ProfileViewModel

@Composable
fun App() {
    val profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val profileUiState by profileViewModel.uiState.collectAsState()

    AppTheme(isDarkMode = profileUiState.isDarkMode) {
        MainScreen(
            profileUiState = profileUiState,
            onToggleDarkMode = { profileViewModel.toggleDarkMode() },
            onToggleEditing = { profileViewModel.toggleEditing() },
            onUpdateProfile = { name, bio -> profileViewModel.updateProfile(name, bio) }
        )
    }
}
