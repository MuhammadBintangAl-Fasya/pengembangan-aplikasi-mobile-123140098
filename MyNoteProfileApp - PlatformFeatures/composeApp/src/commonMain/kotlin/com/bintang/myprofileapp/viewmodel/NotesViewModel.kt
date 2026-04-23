package com.bintang.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.myprofileapp.data.NoteRepository
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.data.SortOrder
import com.bintang.myprofileapp.model.NoteUi
import com.bintang.myprofileapp.model.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<NotesUiState> = combine(
        _searchQuery.debounce(300),
        settingsManager.sortOrderFlow
    ) { query, sortOrder ->
        Pair(query, sortOrder)
    }.flatMapLatest { (query, sortOrder) ->
        if (query.isBlank()) {
            repository.getAllNotes(sortOrder)
        } else {
            repository.searchNotes(query)
        }
    }.map { notes ->
        val uiNotes = notes.map { it.toUi() }
        if (uiNotes.isEmpty()) {
            if (_searchQuery.value.isNotBlank()) {
                NotesUiState.Content(emptyList())
            } else {
                NotesUiState.Empty
            }
        } else {
            NotesUiState.Content(uiNotes)
        }
    }.catch { e ->
        emit(NotesUiState.Error(e.message ?: "Terjadi kesalahan"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState.Loading
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content)
        }
    }

    fun updateNote(noteId: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNote(noteId, title, content)
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    fun toggleFavorite(noteId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(noteId)
        }
    }

    suspend fun getNoteById(noteId: Long): NoteUi? {
        return repository.getNoteById(noteId)?.toUi()
    }

    fun getFavoriteNotes(): Flow<List<NoteUi>> {
        return repository.getAllNotes(settingsManager.sortOrder)
            .map { notes -> notes.filter { it.is_favorite == 1L }.map { it.toUi() } }
    }
}
