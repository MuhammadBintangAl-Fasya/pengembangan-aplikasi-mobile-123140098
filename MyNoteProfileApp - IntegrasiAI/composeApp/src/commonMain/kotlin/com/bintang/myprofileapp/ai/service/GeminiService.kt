package com.bintang.myprofileapp.ai.service

import com.bintang.myprofileapp.ai.config.ApiConfig
import com.bintang.myprofileapp.ai.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GeminiService(private val client: HttpClient) {

    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.5-flash"

    suspend fun generateContent(
        prompt: String,
        systemPrompt: String? = null
    ): Result<String> = safeAICall {
        retryWithBackoff {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt)),
                        role = "user"
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.7,
                    maxOutputTokens = 1000,
                    topP = 0.9
                ),
                systemInstruction = systemPrompt?.let {
                    SystemInstruction(parts = listOf(Part(text = it)))
                }
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

            candidate?.content?.parts?.firstOrNull()?.text
                ?: throw AIError.ParseError("AI tidak memberikan respon (kosong).")
        }
    }
}
