package com.bintang.myprofileapp.ai.service

import com.bintang.myprofileapp.ai.config.ApiConfig
import com.bintang.myprofileapp.ai.model.*
import com.bintang.myprofileapp.ai.prompt.SystemPrompts
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GeminiChatService(private val client: HttpClient) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.5-flash"
    private val conversationHistory = mutableListOf<Content>()

    suspend fun chat(
        userMessage: String,
        noteContext: String? = null
    ): Result<String> = safeAICall {
        retryWithBackoff {
            val messageText = if (conversationHistory.isEmpty() && noteContext != null) {
                """
                [Konteks catatan user saat ini:]
                $noteContext
                
                [Pertanyaan/Pesan user:]
                $userMessage
                """.trimIndent()
            } else {
                userMessage
            }

            conversationHistory.add(
                Content(parts = listOf(Part(text = messageText)), role = "user")
            )

            val request = GeminiRequest(
                contents = conversationHistory.toList(),
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 1000,
                    topP = 0.9
                ),
                systemInstruction = SystemInstruction(
                    parts = listOf(Part(text = SystemPrompts.NOTE_ASSISTANT))
                )
            )

            val response: GeminiResponse = client.post(
                "$baseUrl/models/$model:generateContent"
            ) {
                contentType(ContentType.Application.Json)
                parameter("key", ApiConfig.geminiApiKey)
                setBody(request)
            }.body()

            if (response.error != null) {
                throw AIError.ServerError(response.error.message ?: "Terjadi kesalahan pada server AI")
            }

            val candidate = response.candidates?.firstOrNull()
            if (candidate?.finishReason == "SAFETY") {
                throw AIError.ParseError("Pesan ditolak oleh sistem keamanan AI.")
            }

            val assistantText = candidate?.content?.parts?.firstOrNull()?.text
                ?: throw AIError.ParseError("AI tidak memberikan respon (kosong).")

            conversationHistory.add(
                Content(parts = listOf(Part(text = assistantText)), role = "model")
            )

            assistantText
        }
    }

    fun clearHistory() {
        conversationHistory.clear()
    }

    fun getHistorySize(): Int = conversationHistory.size
}
