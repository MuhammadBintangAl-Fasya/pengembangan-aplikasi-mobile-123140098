# 📰 News Reader App

**Tugas 6 - Pengembangan Aplikasi Mobile**

| | |
|---|---|
| **NIM** | 123140098 |
| **Nama** | Muhammad Bintang Al-Fasya |
| **Kelas** | RA |

---

## 📋 Deskripsi

Aplikasi **News Reader** yang dibangun menggunakan **Compose Multiplatform (KMP)** dengan **Kotlin**. Aplikasi ini mengambil data berita dari internet melalui API dan menampilkannya dalam bentuk list yang interaktif. Pengguna dapat melihat daftar artikel, membaca detail artikel, serta melakukan pull-to-refresh untuk memperbarui data.

---

## 🔗 API yang Digunakan

| API | URL | Deskripsi |
|-----|-----|-----------|
| JSONPlaceholder | `https://jsonplaceholder.typicode.com/posts` | Mengambil daftar artikel (title, body) |
| JSONPlaceholder | `https://jsonplaceholder.typicode.com/posts/{id}` | Mengambil detail artikel berdasarkan ID |
| Picsum Photos | `https://picsum.photos/seed/{id}/600/300` | Gambar placeholder untuk setiap artikel |

---

## 🏗️ Arsitektur & Pattern

Aplikasi menggunakan **MVVM + Repository Pattern** dengan **Dependency Injection manual**:

```
UI Layer (Compose Screens)
    ↓
ViewModel Layer (StateFlow)
    ↓
Repository Layer (NewsRepository)
    ↓
API Layer (NewsApi)
    ↓
Network Layer (Ktor Client → JSONPlaceholder API)
```

### Dependency Injection

Menggunakan `AppModule` sebagai manual DI container yang menyediakan semua dependency secara **lazy initialization**:

```
AppModule (DI Container)
├── HttpClient (via HttpClientFactory)
├── NewsApi (depends on HttpClient)
├── NewsRepository (depends on NewsApi)
└── NewsViewModelFactory (creates NewsViewModel)
```

---

## 📁 Struktur File

```
composeApp/src/commonMain/kotlin/com/bintang/newsreader/
├── data/
│   ├── Article.kt              # Data model (@Serializable) dengan computed properties
│   ├── HttpClientFactory.kt    # Ktor Client setup (JSON, Logging, Timeout)
│   ├── NewsApi.kt              # API calls ke JSONPlaceholder
│   └── NewsRepository.kt       # Repository pattern (Result wrapper)
├── di/
│   └── AppModule.kt            # Manual Dependency Injection container
├── ui/
│   ├── AppNavigation.kt        # Navigation setup (NavHost + Routes)
│   ├── NewsListScreen.kt       # List screen + Pull-to-Refresh + ArticleCard
│   ├── NewsDetailScreen.kt     # Detail screen dengan scrollable content
│   ├── NewsViewModel.kt        # ViewModel (StateFlow untuk articles & refresh)
│   └── UiState.kt              # Sealed class (Loading/Success/Error)
└── App.kt                      # Entry point (MaterialTheme + AppNavigation)
```

---

## 🛠️ Teknologi yang Digunakan

| Teknologi | Kegunaan |
|-----------|----------|
| Kotlin Multiplatform | Shared codebase (Android & Desktop) |
| Compose Multiplatform | UI Framework deklaratif |
| Ktor Client | HTTP networking (OkHttp engine untuk Android) |
| Kotlinx Serialization | JSON parsing (`@Serializable`) |
| Coil 3 | Async image loading dari URL |
| Navigation Compose | Navigasi antar screen (NavHost) |
| Material 3 | Design system & komponen UI |
| Lifecycle ViewModel | State management dengan `StateFlow` |
| Kotlinx Coroutines | Asynchronous programming |

---

## ✨ Fitur

- ✅ **Fetch berita dari API** — Menggunakan Ktor Client ke JSONPlaceholder API
- ✅ **List artikel** — Menampilkan title, deskripsi singkat, gambar, dan badge user
- ✅ **Detail screen** — Tampilan lengkap artikel saat diklik dengan scrollable content
- ✅ **Pull to Refresh** — Menggunakan `PullToRefreshBox` dari Material 3
- ✅ **Loading, Success, Error states** — Menggunakan `UiState` sealed class
- ✅ **Repository pattern** — `NewsRepository` untuk abstraksi API calls dengan `Result` wrapper
- ✅ **Manual Dependency Injection** — `AppModule` sebagai DI container
- ✅ **Navigation** — `NavHost` dengan route arguments (`articleId`)
- ✅ **Image loading** — Coil 3 `AsyncImage` dengan gambar dari Picsum Photos
- ✅ **Error handling** — Tombol "Coba Lagi" pada error state

---

## 📸 Screenshot Semua States

### Loading State
![Android](https://github.com/user-attachments/assets/4c66a7f0-c993-49a9-bd4f-b3556f6e4e74)

### Success State
![Android](https://github.com/user-attachments/assets/478077eb-44de-44bf-b914-86305189bd80)

### Detail Screen
![Android](https://github.com/user-attachments/assets/6dce5f69-0f11-4890-b0a2-f854236449ca)

### Pull to Refresh
![Android](https://github.com/user-attachments/assets/ac24c3e9-8ca9-4238-bbae-959540eba34e)

### Error State
![Android](https://github.com/user-attachments/assets/5cc2a51e-b709-4877-a871-688f38ff73d7)

---

## 🎥 Video Demo

> Video demo ~30 detik menunjukkan alur lengkap:
> **Loading → Success → Klik Artikel → Detail → Back → Pull to Refresh → Airplane Mode → Error → Retry**

🔗 **Link Video Demo:** [Video Demo](https://drive.google.com/file/d/1wJ-q98wbCfzALJFWNm-A6mkJ439rFSkU/view?usp=drive_link)

---

## 🚀 Cara Menjalankan

1. **Clone repository**
   ```bash
   git clone <repository-url>
   ```

2. **Buka project di Android Studio**

3. **Sync Gradle** — Tunggu hingga semua dependency terdownload

4. **Run di emulator/device Android** (min SDK 24)

### Build dari Terminal

- **macOS/Linux:**
  ```bash
  ./gradlew :composeApp:assembleDebug
  ```

- **Windows:**
  ```bash
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Run Desktop (JVM)

- **macOS/Linux:**
  ```bash
  ./gradlew :composeApp:run
  ```

- **Windows:**
  ```bash
  .\gradlew.bat :composeApp:run
  ```