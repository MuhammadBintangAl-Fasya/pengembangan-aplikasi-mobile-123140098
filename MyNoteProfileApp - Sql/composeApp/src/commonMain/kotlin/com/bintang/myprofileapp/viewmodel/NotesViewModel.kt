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

/**
 * NotesViewModel — mengelola state Notes list dengan SQLDelight database.
 *
 * Fitur:
 * - CRUD operations melalui NoteRepository
 * - Search dengan debounce 300ms
 * - Sort order dari SettingsManager
 * - Proper UI states (Loading, Empty, Content, Error)
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class NotesViewModel(
    private val repository: NoteRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    // ─── SEARCH STATE ────────────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ─── UI STATE ────────────────────────────────────────────

    /**
     * Main UI state yang menggabungkan notes dari database dengan search query.
     * Flow otomatis update ketika:
     * - Data di database berubah (insert/update/delete)
     * - Search query berubah
     * - Sort order berubah
     */
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
                NotesUiState.Content(emptyList()) // Search no results — different from truly empty
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

    // ─── SEARCH ──────────────────────────────────────────────

    /**
     * Update search query — debounced 300ms untuk performa.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Clear search query.
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    // ─── CRUD OPERATIONS ─────────────────────────────────────

    /**
     * Tambah note baru.
     */
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(title, content)
        }
    }

    /**
     * Update note yang sudah ada.
     */
    fun updateNote(noteId: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNote(noteId, title, content)
        }
    }

    /**
     * Hapus note berdasarkan ID.
     */
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    /**
     * Toggle status favorite note.
     */
    fun toggleFavorite(noteId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(noteId)
        }
    }

    /**
     * Ambil note berdasarkan ID (untuk detail/edit screen).
     */
    suspend fun getNoteById(noteId: Long): NoteUi? {
        return repository.getNoteById(noteId)?.toUi()
    }

    /**
     * Ambil semua favorite notes sebagai Flow.
     */
    fun getFavoriteNotes(): Flow<List<NoteUi>> {
        return repository.getAllNotes(settingsManager.sortOrder)
            .map { notes -> notes.filter { it.is_favorite == 1L }.map { it.toUi() } }
    }
}
