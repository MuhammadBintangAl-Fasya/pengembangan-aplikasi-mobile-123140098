package com.bintang.myprofileapp.ai.config

actual object ApiConfig {
    actual val geminiApiKey: String = System.getenv("GEMINI_API_KEY") ?: ""
}
