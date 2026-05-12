package com.bintang.myprofileapp.ai.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: SystemInstruction? = null
)

@Serializable
data class SystemInstruction(
    val parts: List<Part>
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String = "user"
)

@Serializable
data class Part(
    val text: String? = null
)

@Serializable
data class GenerationConfig(
    val temperature: Double = 0.7,
    val maxOutputTokens: Int = 1000,
    val topP: Double = 0.9
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>? = null,
    val error: GeminiErrorResponse? = null
)

@Serializable
data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null
)

@Serializable
data class GeminiErrorResponse(
    val code: Int? = null,
    val message: String? = null,
    val status: String? = null
)

data class ChatMessageUi(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = 0L,
    val isError: Boolean = false
)
