package com.bintang.myprofileapp.viewmodel

import com.bintang.myprofileapp.model.NoteUi

/**
 * Sealed interface untuk mengelola UI states yang proper.
 * Setiap state merepresentasikan kondisi berbeda di layar:
 * - Loading: data sedang dimuat dari database
 * - Empty: tidak ada notes sama sekali
 * - Content: ada data notes untuk ditampilkan
 * - Error: terjadi kesalahan saat memuat data
 */
sealed interface NotesUiState {

    /** Data sedang dimuat dari SQLDelight database */
    data object Loading : NotesUiState

    /** Database tidak berisi notes apapun */
    data object Empty : NotesUiState

    /** Berhasil memuat data notes */
    data class Content(val notes: List<NoteUi>) : NotesUiState

    /** Terjadi error saat memuat data */
    data class Error(val message: String) : NotesUiState
}
