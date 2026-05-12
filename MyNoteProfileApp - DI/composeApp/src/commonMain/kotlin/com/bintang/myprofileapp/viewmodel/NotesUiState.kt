package com.bintang.myprofileapp.viewmodel

import com.bintang.myprofileapp.model.NoteUi

sealed interface NotesUiState {
    data object Loading : NotesUiState
    data object Empty : NotesUiState
    data class Content(val notes: List<NoteUi>) : NotesUiState
    data class Error(val message: String) : NotesUiState
}
