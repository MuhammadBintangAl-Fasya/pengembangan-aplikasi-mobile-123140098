package com.bintang.myprofileapp.navigation

/**
 * Sealed class mendefinisikan semua routes navigasi di aplikasi.
 * Menggunakan sealed class untuk type-safety dan centralized route management.
 */
sealed class Screen(val route: String) {

    /** Notes list — tab utama di Bottom Navigation */
    object NoteList : Screen("note_list")

    /** Note detail — menerima noteId sebagai required argument */
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }

    /** Add note — form untuk membuat note baru */
    object AddNote : Screen("add_note")

    /** Edit note — form edit dengan noteId sebagai argument */
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }

    /** Favorites — tab kedua di Bottom Navigation */
    object Favorites : Screen("favorites")

    /** Profile — tab ketiga di Bottom Navigation */
    object Profile : Screen("profile")
}
