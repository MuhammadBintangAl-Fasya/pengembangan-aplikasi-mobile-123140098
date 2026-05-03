# 🤖 Notes App — Tugas 9: Integrasi AI

**Tugas 9 - Pengembangan Aplikasi Mobile**  
**Nama:** Muhammad Bintang Alfasya  
**NIM:** 123140098  
**Kelas:** RA

---

## 📋 Deskripsi

Upgrade aplikasi **Notes & Profile** berbasis **Kotlin Multiplatform (KMP)** dengan **Integrasi AI** menggunakan **Google Gemini API**:

- ✨ **AI Chat (Multi-turn Conversation)** — Asisten cerdas yang bisa menjawab pertanyaan tentang catatan user
- 📝 **Content Summarization** — Rangkum catatan secara otomatis menggunakan AI
- 🎯 **Smart Recommendations** — Rekomendasi personal berdasarkan catatan yang ada
- 🔄 **Proper Error Handling** — Sealed class errors, retry with exponential backoff
- 💬 **Loading States & UI Feedback** — Typing indicator animation, snackbar errors

---

## 🏗️ Arsitektur

```text
┌─────────────────────────────────────────────────┐
│                   UI Layer                       │
│  (Compose Screens + Navigation + koinInject())   │
│  AIChatScreen, NoteSummaryScreen, ChatBubble,    │
│  TypingIndicator                                 │
├─────────────────────────────────────────────────┤
│                ViewModel Layer                   │
│  AIViewModel (ChatUiState, SummaryUiState)       │
│  NotesViewModel, SettingsViewModel               │
├─────────────────────────────────────────────────┤
│              Repository / Services               │
│  AIRepository → GeminiService, GeminiChatService │
│  NoteRepository, SettingsManager                 │
├─────────────────────────────────────────────────┤
│           AI Service Layer (Ktor)                │
│  HttpClientFactory, SystemPrompts,               │
│  GeminiModels, AIError + RetryWithBackoff        │
├─────────────────────────────────────────────────┤
│         Koin DI Container (AppModule)            │
│  commonModule → platformModule (expect/actual)   │
├─────────────────────────────────────────────────┤
│               Data / Platform Layer              │
│  SQLDelight DB, multiplatform-settings,          │
│  ApiConfig (expect/actual), BuildConfig          │
└─────────────────────────────────────────────────┘
```

### AI Service Flow

```text
User Input → AIViewModel → AIRepository → GeminiService/GeminiChatService
                                              ↓
                                     Ktor HttpClient
                                              ↓
                                  Gemini API (REST)
                                              ↓
                                    AI Response (JSON)
                                              ↓
                              Parse → Update UI State
```

---

## 🤖 Fitur AI

### 1. Content Summarization (📝)
- Buka detail catatan → tap tombol ✨ (AutoAwesome) di toolbar
- AI merangkum catatan dalam 2-3 kalimat dengan poin kunci
- Loading state dan retry jika gagal

### 2. Smart Recommendations (🎯)
- Buka AI Chat → tap "Rekomendasi Personal"
- AI menganalisis semua catatan dan memberikan saran:
  - Topik baru yang bisa dieksplorasi
  - Catatan yang saling terkait
  - Ide catatan baru berdasarkan pattern

### 3. Multi-turn Chat (💬) — Bonus +5%
- Full chat interface dengan conversation history
- AI memiliki konteks catatan user
- Welcome screen dengan quick action cards
- Typing indicator animation saat AI memproses

---

## 🛡️ Error Handling

```kotlin
sealed class AIError : Exception() {
    data class Unauthorized(...)     // 401 - API key invalid
    data class RateLimited(...)      // 429 - Too many requests
    data class ServerError(...)      // 500+ - Server down
    data class NetworkError(...)     // IOException - No internet
    data class ParseError(...)       // SerializationException
    data class Unknown(...)          // Catch-all
}
```

- **`safeAICall()`** — Wraps API calls, maps exceptions to `AIError`
- **`retryWithBackoff()`** — Auto-retry 3x dengan exponential backoff untuk transient errors
- **UI Feedback** — Snackbar untuk errors, retry button di summary screen

---

## 🎨 Prompt Engineering

System prompts dirancang dengan teknik:
- **Role Definition** — "Kamu adalah asisten AI yang ahli..."
- **Task Specification** — Tugas spesifik yang harus dilakukan
- **Output Format** — Format output yang diharapkan (emoji, bullet points)
- **Constraints** — Batasan (bahasa, panjang, behavior)
- **Examples** — Contoh format output

Tersedia di `SystemPrompts.kt`:
- `NOTE_SUMMARIZER` — Prompt untuk merangkum catatan
- `NOTE_RECOMMENDER` — Prompt untuk rekomendasi personal
- `NOTE_ASSISTANT` — Prompt untuk asisten chat

---

## 📂 Struktur File

```
composeApp/src/
├── commonMain/kotlin/com/bintang/myprofileapp/
│   ├── App.kt
│   ├── ai/                              ← NEW: AI Integration Layer
│   │   ├── config/
│   │   │   └── ApiConfig.kt             ← expect object ApiConfig
│   │   ├── model/
│   │   │   ├── GeminiModels.kt          ← Request/Response DTOs
│   │   │   └── AIError.kt              ← Sealed class errors + retry
│   │   ├── network/
│   │   │   └── HttpClientFactory.kt     ← Ktor client configuration
│   │   ├── prompt/
│   │   │   └── SystemPrompts.kt         ← Well-designed system prompts
│   │   ├── service/
│   │   │   ├── GeminiService.kt         ← Single-turn generation
│   │   │   └── GeminiChatService.kt     ← Multi-turn conversation
│   │   └── repository/
│   │       ├── AIRepository.kt          ← Interface abstraksi
│   │       └── AIRepositoryImpl.kt      ← Implementation
│   ├── di/
│   │   └── AppModule.kt                 ← + AI dependencies (Koin)
│   ├── viewmodel/
│   │   ├── AIViewModel.kt              ← NEW: AI state management
│   │   ├── NotesViewModel.kt
│   │   ├── SettingsViewModel.kt
│   │   └── ...
│   ├── navigation/
│   │   ├── Screen.kt                    ← + AIChat, NoteSummary routes
│   │   ├── AppNavigation.kt            ← + AI screen composables
│   │   └── BottomNavItem.kt            ← + AI Chat nav item
│   ├── screens/
│   │   ├── AIChatScreen.kt             ← NEW: Chat interface
│   │   ├── NoteSummaryScreen.kt        ← NEW: AI summary display
│   │   ├── NoteDetailScreen.kt         ← + Summarize button
│   │   └── ...
│   └── ui/components/
│       ├── ChatBubble.kt               ← NEW: Chat bubble composable
│       ├── TypingIndicator.kt          ← NEW: Animated typing dots
│       └── ...
├── androidMain/kotlin/com/bintang/myprofileapp/
│   ├── ai/config/
│   │   └── ApiConfig.android.kt        ← actual (BuildConfig)
│   └── ...
└── jvmMain/kotlin/com/bintang/myprofileapp/
    ├── ai/config/
    │   └── ApiConfig.jvm.kt            ← actual (env variable)
    └── ...
```

---

## 🛠️ Tech Stack

| Teknologi | Kegunaan |
|-----------|----------|
| Kotlin Multiplatform | Shared code Android + Desktop |
| Compose Multiplatform | UI Declarative multi-platform |
| Koin 4.0 | Dependency Injection framework |
| SQLDelight | Local database type-safe |
| Multiplatform Settings | Key-value preferences |
| Navigation Compose | Screen routing |
| Material 3 | Design system |
| **Ktor Client** | **HTTP client untuk Gemini API** |
| **Kotlinx Serialization** | **JSON parsing untuk API** |
| **Google Gemini 2.0 Flash** | **AI/LLM API (gratis)** |

---

## ⚙️ Setup API Key

1. Buka [Google AI Studio](https://aistudio.google.com)
2. Sign in dengan akun Google
3. Klik "Get API key" → Create API key
4. Buat file `local.properties` di root project:

```properties
GEMINI_API_KEY=your_api_key_here
```

> ⚠️ **PENTING:** File `local.properties` sudah ada di `.gitignore`. JANGAN commit API key ke repository!

---

## 🚀 Cara Menjalankan

1. Clone repository
2. Setup API key (lihat bagian Setup API Key di atas)
3. Buka project di Android Studio
4. Sync Gradle dependencies
5. Run di Android emulator atau Desktop (JVM)

## Dokumentasi

1. AI SUMMARY:

![Klik Tombol AI Summary](https://github.com/user-attachments/assets/1638e963-73c4-4584-90a3-1f9c6fc21182)

![Hasil AI Summary](https://github.com/user-attachments/assets/858b78cd-d81b-4439-b6e5-868f252f10a1)

2. AI CHAT:

![Hasil AI Chat](https://github.com/user-attachments/assets/590e8eec-e121-42b0-8af1-c321d25bf94d)