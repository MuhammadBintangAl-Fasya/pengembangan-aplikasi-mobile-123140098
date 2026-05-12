# 🧪 Notes App — Tugas 10: Testing & Dependency Injection

**Tugas 10 - Pengembangan Aplikasi Mobile**  
**Nama:** Muhammad Bintang Alfasya  
**NIM:** 123140098  
**Kelas:** RA

---

## 📋 Deskripsi

Implementasi **Dependency Injection** dan **Testing** untuk aplikasi **Notes & Profile** berbasis **Kotlin Multiplatform (KMP)**:

- 💉 **Koin DI** — Setup 2 modules (`dataModule`, `viewModelModule`) dengan proper injection
- 🧪 **Unit Test Repository** — 9 test cases untuk `NoteRepository` (CRUD, search, sort)
- 🎯 **Unit Test ViewModel** — 10 test cases dengan MockK untuk `NotesViewModel`
- 🌊 **Flow Test Turbine** — Test Flow emissions pada ViewModel state
- 📱 **UI Test Compose** — 5 test cases untuk `NoteListScreen` (loading, empty, content, error, FAB)
- 📊 **Code Coverage** — Target ≥60% untuk business logic

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
│  dataModule + viewModelModule + platformModule   │
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

## 💉 Koin DI Setup

### Module Structure

| Module | Isi | Tipe |
|--------|-----|------|
| `dataModule` | Database, Repository, SettingsManager, AI Services | `single` |
| `viewModelModule` | NotesViewModel, SettingsViewModel, ProfileViewModel, AIViewModel | `viewModel` |
| `platformModule` | DatabaseDriverFactory, DeviceInfo, NetworkMonitor, BatteryInfo | `single` (platform-specific) |

### Interface-based DI

`NoteRepository` di-refactor menjadi **interface** + **implementation** (`NoteRepositoryImpl`) agar bisa di-mock saat testing.

```
Koin Graph:
dataModule ──→ NotesDatabase ──→ NoteRepositoryImpl (as NoteRepository)
                                       ↓
viewModelModule ──→ NotesViewModel(repository, settingsManager)
```

---

## 🧪 Daftar Test Cases

### NoteRepositoryTest (9 test cases)

| # | Test Case | Deskripsi |
|---|-----------|----------|
| 1 | `insertNote and getAllNotes returns inserted note` | Insert note lalu verifikasi ada di list |
| 2 | `getNoteById returns correct note` | Ambil note berdasarkan ID |
| 3 | `getNoteById returns null for non-existent id` | Return null untuk ID yang tidak ada |
| 4 | `updateNote updates title and content` | Update note lalu verifikasi perubahan |
| 5 | `deleteNote removes note from database` | Hapus note lalu verifikasi hilang |
| 6 | `toggleFavorite changes favorite status` | Toggle favorite 0→1→0 |
| 7 | `searchNotes finds matching notes by title` | Search berdasarkan title |
| 8 | `searchNotes finds matching notes by content` | Search berdasarkan content |
| 9 | `getAllNotes with different sort orders` | Verifikasi sorting ASC/DESC |

### NotesViewModelTest (10 test cases — MockK + Turbine)

| # | Test Case | Library |
|---|-----------|----------|
| 1 | `initial uiState is Loading` | MockK |
| 2 | `uiState emits Content when notes available` | MockK + Turbine |
| 3 | `uiState emits Empty when no notes` | MockK + Turbine |
| 4 | `addNote calls repository insertNote` | MockK (coVerify) |
| 5 | `deleteNote calls repository deleteNote` | MockK (coVerify) |
| 6 | `toggleFavorite calls repository toggleFavorite` | MockK (coVerify) |
| 7 | `getNoteById returns mapped NoteUi` | MockK |
| 8 | `getNoteById returns null for non-existent note` | MockK |
| 9 | `onSearchQueryChange updates searchQuery` | MockK |
| 10 | `clearSearch resets searchQuery to empty` | MockK |

### NotesScreenTest (5 UI test cases — Compose Test)

| # | Test Case | Assertion |
|---|-----------|----------|
| 1 | `loadingState showsLoadingIndicator` | `LOADING_STATE` tag displayed |
| 2 | `emptyState showsEmptyMessage` | `EMPTY_STATE` tag + text displayed |
| 3 | `contentState showsNotesList` | `NOTES_LIST` tag + note titles |
| 4 | `addButton isDisplayed` | `ADD_BUTTON` tag displayed |
| 5 | `errorState showsErrorMessage` | `ERROR_STATE` tag + error text |

**Total: 24 test cases**

---

## 📊 Test Coverage

Target: **≥60%** untuk business logic (`NoteRepository`, `NotesViewModel`)

Jalankan test:
```bash
./gradlew jvmTest
```

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
│   │   └── AppModule.kt                 ← dataModule + viewModelModule
│   ├── data/
│   │   ├── NoteRepository.kt            ← Interface + NoteRepositoryImpl
│   │   └── SettingsManager.kt
│   ├── viewmodel/
│   │   ├── NotesViewModel.kt
│   │   ├── NotesUiState.kt
│   │   └── ...
│   ├── screens/
│   │   ├── NoteListScreen.kt            ← + testTag modifiers
│   │   └── ...
│   └── ui/
│       ├── TestTags.kt                  ← Test tag constants
│       └── components/
│           └── ...
├── jvmTest/kotlin/com/bintang/myprofileapp/  ← TEST FILES
│   ├── data/
│   │   └── NoteRepositoryTest.kt        ← 9 test cases
│   ├── viewmodel/
│   │   └── NotesViewModelTest.kt        ← 10 test cases (MockK+Turbine)
│   └── screens/
│       └── NotesScreenTest.kt           ← 5 UI test cases
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
| **MockK 1.13.9** | **Mocking library untuk unit test** |
| **Turbine 1.0.0** | **Flow testing library** |
| **Coroutines Test** | **Testing coroutines & dispatchers** |
| **Compose UI Test** | **UI testing untuk Compose** |
| Multiplatform Settings | Key-value preferences |
| Navigation Compose | Screen routing |
| Material 3 | Design system |
| Ktor Client | HTTP client untuk Gemini API |
| Kotlinx Serialization | JSON parsing |

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