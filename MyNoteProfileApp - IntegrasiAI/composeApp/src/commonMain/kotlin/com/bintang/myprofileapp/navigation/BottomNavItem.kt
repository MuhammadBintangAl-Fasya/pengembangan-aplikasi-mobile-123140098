package com.bintang.myprofileapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Notes : BottomNavItem(
        route = Screen.NoteList.route,
        icon = Icons.Default.Home,
        label = "Notes"
    )

    object Favorites : BottomNavItem(
        route = Screen.Favorites.route,
        icon = Icons.Default.Favorite,
        label = "Favorites"
    )

    object AIChat : BottomNavItem(
        route = Screen.AIChat.route,
        icon = Icons.Default.AutoAwesome,
        label = "AI Chat"
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        icon = Icons.Default.Person,
        label = "Profile"
    )
}
