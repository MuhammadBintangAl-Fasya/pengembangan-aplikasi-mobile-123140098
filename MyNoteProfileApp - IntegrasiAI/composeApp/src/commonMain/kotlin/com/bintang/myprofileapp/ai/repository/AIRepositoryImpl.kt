package com.bintang.myprofileapp.ai.repository

import com.bintang.myprofileapp.ai.prompt.SystemPrompts
import com.bintang.myprofileapp.ai.service.GeminiChatService
import com.bintang.myprofileapp.ai.service.GeminiService
import com.bintang.myprofileapp.model.NoteUi

class AIRepositoryImpl(
    private val geminiService: GeminiService,
    private val chatService: GeminiChatService
) : AIRepository {

    override suspend fun summarizeNote(title: String, content: String): Result<String> {
        val prompt = """
            Judul Catatan: $title
            
            Isi Catatan:
            $content
        """.trimIndent()

        return geminiService.generateContent(
            prompt = prompt,
            systemPrompt = SystemPrompts.NOTE_SUMMARIZER
        )
    }

    override suspend fun getRecommendations(notes: List<NoteUi>): Result<String> {
        val notesContext = if (notes.isEmpty()) {
            "User belum memiliki catatan apapun."
        } else {
            notes.take(10).joinToString("\n\n") { note ->
                """
                📌 Judul: ${note.title}
                Isi: ${note.content.take(200)}${if (note.content.length > 200) "..." else ""}
                Favorit: ${if (note.isFavorite) "Ya" else "Tidak"}
                """.trimIndent()
            }
        }

        val prompt = """
            Berikut adalah daftar catatan user (${notes.size} catatan):
            
            $notesContext
            
            Berikan rekomendasi personal berdasarkan catatan-catatan di atas.
        """.trimIndent()

        return geminiService.generateContent(
            prompt = prompt,
            systemPrompt = SystemPrompts.NOTE_RECOMMENDER
        )
    }

    override suspend fun chat(message: String, notes: List<NoteUi>): Result<String> {
        val noteContext = if (notes.isNotEmpty()) {
            val notesSummary = notes.take(5).joinToString("\n") { note ->
                "- ${note.title}: ${note.content.take(100)}..."
            }
            "User memiliki ${notes.size} catatan. Beberapa di antaranya:\n$notesSummary"
        } else {
            "User belum memiliki catatan."
        }

        return chatService.chat(
            userMessage = message,
            noteContext = noteContext
        )
    }

    override fun clearChatHistory() {
        chatService.clearHistory()
    }
}
