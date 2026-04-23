# 📝 Notes App — Tugas 8

**Tugas 8 - Pengembangan Aplikasi Mobile**  
**Nama:** Muhammad Bintang Alfasya  
**NIM:** 123140098  
**Kelas:** RA

---

## 📋 Deskripsi

Upgrade aplikasi **Notes & Profile** berbasis **Kotlin Multiplatform (KMP)** dengan **Platform-Specific Features**:

- **Koin Dependency Injection** — seluruh dependencies di-inject melalui Koin framework
- **DeviceInfo** dengan expect/actual pattern — menampilkan nama perangkat, OS, dan versi aplikasi
- **NetworkMonitor** dengan expect/actual pattern — deteksi status koneksi internet secara real-time
- **BatteryInfo** dengan expect/actual pattern (bonus) — menampilkan level baterai dan status charging
- **Device Info** ditampilkan di Settings screen
- **Network Status Indicator** ditampilkan di main screen (NoteListScreen)

---

## 🏗️ Arsitektur

```text
┌─────────────────────────────────────────────────┐
│                   UI Layer                       │
│  (Compose Screens + Navigation + koinInject())   │
├─────────────────────────────────────────────────┤
│                ViewModel Layer                   │
│  (koinViewModel() — StateFlow + UI States)       │
├─────────────────────────────────────────────────┤
│              Repository / Services               │
│  NoteRepository, SettingsManager, DeviceInfo,    │
│  NetworkMonitor, BatteryInfo                     │
├─────────────────────────────────────────────────┤
│         Koin DI Container (AppModule)            │
│  commonModule → platformModule (expect/actual)   │
├─────────────────────────────────────────────────┤
│               Data / Platform Layer              │
│  SQLDelight DB, multiplatform-settings,          │
│  Android APIs (Build, ConnectivityManager,       │
│  BatteryManager) / JVM APIs (System Props)       │
└─────────────────────────────────────────────────┘
```

### Koin DI Flow

```text
startKoin {
  modules(commonModule, platformModule)
}

commonModule:
  Settings → SettingsManager
  DatabaseDriverFactory → SqlDriver → NotesDatabase → NoteRepository
  ViewModels: NotesViewModel, SettingsViewModel, ProfileViewModel

platformModule (Android):
  DatabaseDriverFactory(androidContext())
  BatteryInfo(androidContext())
  NetworkMonitor(androidContext())
  DeviceInfo()

platformModule (JVM):
  DatabaseDriverFactory()
  BatteryInfo()
  NetworkMonitor()
  DeviceInfo()
```

---

## 📂 Struktur File

```
composeApp/src/
├── commonMain/kotlin/com/bintang/myprofileapp/
│   ├── App.kt
│   ├── di/
│   │   └── AppModule.kt                 ← Koin common + expect platformModule
│   ├── platform/
│   │   ├── DeviceInfo.kt                ← expect class DeviceInfo
│   │   ├── NetworkMonitor.kt            ← expect class NetworkMonitor
│   │   └── BatteryInfo.kt               ← expect class BatteryInfo
│   ├── data/
│   │   ├── NoteRepository.kt
│   │   └── SettingsManager.kt
│   ├── db/
│   │   └── DatabaseDriverFactory.kt     ← expect class DatabaseDriverFactory
│   ├── model/
│   │   ├── Note.kt
│   │   └── ProfileData.kt
│   ├── viewmodel/
│   │   ├── NotesViewModel.kt
│   │   ├── SettingsViewModel.kt
│   │   ├── ProfileViewModel.kt
│   │   └── NotesUiState.kt
│   ├── navigation/
│   │   ├── Screen.kt
│   │   ├── AppNavigation.kt
│   │   └── BottomNavItem.kt
│   ├── screens/
│   │   ├── NoteListScreen.kt            ← + NetworkStatusIndicator
│   │   ├── SettingsScreen.kt            ← + DeviceInfo + BatteryInfo sections
│   │   ├── FavoritesScreen.kt
│   │   ├── NoteDetailScreen.kt
│   │   ├── AddNoteScreen.kt
│   │   ├── EditNoteScreen.kt
│   │   └── ProfileScreen.kt
│   └── ui/
│       ├── components/
│       │   ├── NoteCard.kt
│       │   ├── EmptyState.kt
│       │   └── LoadingState.kt
│       └── theme/
│           └── AppTheme.kt
├── androidMain/kotlin/com/bintang/myprofileapp/
│   ├── MainActivity.kt                  ← startKoin + androidContext()
│   ├── Platform.android.kt
│   ├── di/
│   │   └── PlatformModule.android.kt    ← actual platformModule (Android)
│   ├── db/
│   │   └── DatabaseDriverFactory.android.kt
│   └── platform/
│       ├── DeviceInfo.android.kt        ← Build.MODEL, Build.VERSION
│       ├── NetworkMonitor.android.kt    ← ConnectivityManager
│       └── BatteryInfo.android.kt       ← BatteryManager
└── jvmMain/kotlin/com/bintang/myprofileapp/
    ├── main.kt                           ← startKoin()
    ├── Platform.jvm.kt
    ├── di/
    │   └── PlatformModule.jvm.kt         ← actual platformModule (JVM)
    ├── db/
    │   └── DatabaseDriverFactory.jvm.kt
    └── platform/
        ├── DeviceInfo.jvm.kt             ← System.getProperty()
        ├── NetworkMonitor.jvm.kt         ← InetAddress polling
        └── BatteryInfo.jvm.kt            ← Stub (desktop)
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


---

## 📸 Screenshot

### 1. Settings Screen — Device Info (via Koin DI)
> Menampilkan informasi perangkat (nama, OS, versi) yang di-inject menggunakan Koin `koinInject<DeviceInfo>()`

![Settings - Device Info](https://github.com/user-attachments/assets/d212509e-8f97-4c23-9373-27736aad7c6d)

### 2. Network Status — Online ✅
> Indikator hijau di main screen saat perangkat terhubung ke internet

![Network Online](https://github.com/user-attachments/assets/48955d8b-b7cb-4aa1-b632-4808e3669218)

### 3. Network Status — Offline ❌
> Indikator merah di main screen saat perangkat tidak ada koneksi internet

![Network Offline](https://github.com/user-attachments/assets/886dbb66-44c8-4277-9c21-bfde2ad53923)

### 4. Notes List Screen
> Tampilan utama Notes dengan Network Status Indicator di bagian atas

![Notes List](https://github.com/user-attachments/assets/3059d13f-5570-4572-ac1d-5ac529b8d550)

---

## 🎥 Demo Video

🔗 **Link Video Demo:** [VideoDemo](https://drive.google.com/file/d/1GCEDwPWYN0SHsgfhpkcY4r8dOzXY84rs/view?usp=sharing)

---

## 🚀 Cara Menjalankan

1. Clone repository
2. Buka project di Android Studio
3. Sync Gradle dependencies
4. Run di Android emulator atau Desktop (JVM)
