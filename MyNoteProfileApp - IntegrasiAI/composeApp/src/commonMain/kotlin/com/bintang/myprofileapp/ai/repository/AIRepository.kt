package com.bintang.myprofileapp.ai.repository

import com.bintang.myprofileapp.model.NoteUi

interface AIRepository {
    suspend fun summarizeNote(title: String, content: String): Result<String>
    suspend fun getRecommendations(notes: List<NoteUi>): Result<String>
    suspend fun chat(message: String, notes: List<NoteUi>): Result<String>
    fun clearChatHistory()
}
