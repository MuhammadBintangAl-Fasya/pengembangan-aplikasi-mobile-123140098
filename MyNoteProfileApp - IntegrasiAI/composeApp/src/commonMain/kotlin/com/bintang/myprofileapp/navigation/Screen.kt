package com.bintang.myprofileapp.navigation

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Settings : Screen("settings")

    // AI Screens
    object AIChat : Screen("ai_chat")
    object NoteSummary : Screen("note_summary/{noteId}") {
        fun createRoute(noteId: Long) = "note_summary/$noteId"
    }
}
