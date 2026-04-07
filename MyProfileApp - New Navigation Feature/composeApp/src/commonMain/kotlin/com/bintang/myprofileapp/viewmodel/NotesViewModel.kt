package com.bintang.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.bintang.myprofileapp.model.Note
import com.bintang.myprofileapp.model.sampleNotes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel : ViewModel() {

    private val _notes = MutableStateFlow(sampleNotes)
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private var nextId = sampleNotes.maxOf { it.id } + 1

    fun getNoteById(noteId: Int): Note? {
        return _notes.value.find { it.id == noteId }
    }

    fun getFavoriteNotes(): List<Note> {
        return _notes.value.filter { it.isFavorite }
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = nextId++,
            title = title,
            content = content,
            timestamp = getCurrentTimestamp(),
            isFavorite = false
        )
        _notes.update { currentList ->
            listOf(newNote) + currentList
        }
    }

    fun updateNote(noteId: Int, title: String, content: String) {
        _notes.update { currentList ->
            currentList.map { note ->
                if (note.id == noteId) {
                    note.copy(
                        title = title,
                        content = content,
                        timestamp = getCurrentTimestamp()
                    )
                } else note
            }
        }
    }

    fun deleteNote(noteId: Int) {
        _notes.update { currentList ->
            currentList.filter { it.id != noteId }
        }
    }

    fun toggleFavorite(noteId: Int) {
        _notes.update { currentList ->
            currentList.map { note ->
                if (note.id == noteId) {
                    note.copy(isFavorite = !note.isFavorite)
                } else note
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        // Simple timestamp for demo purposes
        return "6 Apr 2026, 20:00"
    }
}
