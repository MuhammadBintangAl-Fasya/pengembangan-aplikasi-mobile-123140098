package com.bintang.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.myprofileapp.ai.model.ChatMessageUi
import com.bintang.myprofileapp.ai.repository.AIRepository
import com.bintang.myprofileapp.data.NoteRepository
import com.bintang.myprofileapp.model.NoteUi
import com.bintang.myprofileapp.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class AIViewModel(
    private val aiRepository: AIRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()

    private val _summaryState = MutableStateFlow(SummaryUiState())
    val summaryState: StateFlow<SummaryUiState> = _summaryState.asStateFlow()

    private var cachedNotes: List<NoteUi> = emptyList()

    init {
        loadNotesContext()
    }

    private fun loadNotesContext() {
        viewModelScope.launch {
            noteRepository.getAllNotes().collect { notes ->
                cachedNotes = notes.map { it.toUi() }
            }
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        val userMessage = ChatMessageUi(
            text = message,
            isUser = true,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )

        _chatState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            aiRepository.chat(message, cachedNotes)
                .onSuccess { response ->
                    val aiMessage = ChatMessageUi(
                        text = response,
                        isUser = false,
                        timestamp = Clock.System.now().toEpochMilliseconds()
                    )
                    _chatState.update { state ->
                        state.copy(
                            messages = state.messages + aiMessage,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = ChatMessageUi(
                        text = error.message ?: "Terjadi kesalahan",
                        isUser = false,
                        timestamp = Clock.System.now().toEpochMilliseconds(),
                        isError = true
                    )
                    _chatState.update { state ->
                        state.copy(
                            messages = state.messages + errorMessage,
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun requestRecommendations() {
        val promptMessage = ChatMessageUi(
            text = "🎯 Berikan rekomendasi personal berdasarkan catatan saya",
            isUser = true,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )

        _chatState.update { state ->
            state.copy(
                messages = state.messages + promptMessage,
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            aiRepository.getRecommendations(cachedNotes)
                .onSuccess { response ->
                    val aiMessage = ChatMessageUi(
                        text = response,
                        isUser = false,
                        timestamp = Clock.System.now().toEpochMilliseconds()
                    )
                    _chatState.update { state ->
                        state.copy(
                            messages = state.messages + aiMessage,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = ChatMessageUi(
                        text = error.message ?: "Gagal mendapatkan rekomendasi",
                        isUser = false,
                        timestamp = Clock.System.now().toEpochMilliseconds(),
                        isError = true
                    )
                    _chatState.update { state ->
                        state.copy(
                            messages = state.messages + errorMessage,
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun clearChat() {
        aiRepository.clearChatHistory()
        _chatState.value = ChatUiState()
    }

    fun clearError() {
        _chatState.update { it.copy(error = null) }
    }

    fun summarizeNote(noteId: Long) {
        _summaryState.update {
            it.copy(isLoading = true, error = null, summary = null)
        }

        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId)?.toUi()
            if (note == null) {
                _summaryState.update {
                    it.copy(isLoading = false, error = "Catatan tidak ditemukan")
                }
                return@launch
            }

            _summaryState.update {
                it.copy(noteTitle = note.title, noteContent = note.content)
            }

            aiRepository.summarizeNote(note.title, note.content)
                .onSuccess { summary ->
                    _summaryState.update {
                        it.copy(isLoading = false, summary = summary, error = null)
                    }
                }
                .onFailure { error ->
                    _summaryState.update {
                        it.copy(isLoading = false, error = error.message ?: "Gagal merangkum")
                    }
                }
        }
    }

    fun retrySummary() {
        val noteTitle = _summaryState.value.noteTitle ?: return
        val noteContent = _summaryState.value.noteContent ?: return

        _summaryState.update {
            it.copy(isLoading = true, error = null)
        }

        viewModelScope.launch {
            aiRepository.summarizeNote(noteTitle, noteContent)
                .onSuccess { summary ->
                    _summaryState.update {
                        it.copy(isLoading = false, summary = summary, error = null)
                    }
                }
                .onFailure { error ->
                    _summaryState.update {
                        it.copy(isLoading = false, error = error.message ?: "Gagal merangkum")
                    }
                }
        }
    }

    fun clearSummary() {
        _summaryState.value = SummaryUiState()
    }
}

data class ChatUiState(
    val messages: List<ChatMessageUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SummaryUiState(
    val noteTitle: String? = null,
    val noteContent: String? = null,
    val summary: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
