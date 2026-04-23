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

## ✅ Checklist Tugas

### Koin DI Setup (25%)
- ✅ commonModule: Settings, NotesDatabase, NoteRepository, SettingsManager, ViewModels
- ✅ platformModule (expect/actual): DatabaseDriverFactory, DeviceInfo, NetworkMonitor, BatteryInfo
- ✅ Inisialisasi Koin di MainActivity (Android) dan main.kt (JVM)
- ✅ Semua dependencies di-inject via koinViewModel() dan koinInject()

### expect/actual Pattern (25%)
- ✅ DeviceInfo: getDeviceName(), getOsVersion(), getAppVersion()
- ✅ NetworkMonitor: isConnected(), observeConnectivity() → Flow<Boolean>
- ✅ DatabaseDriverFactory: createDriver() → SqlDriver

### UI Integration (20%)
- ✅ Device Info ditampilkan di Settings screen (nama perangkat, OS, versi app)
- ✅ Battery Info ditampilkan di Settings screen (level, status charging, icon dinamis)
- ✅ Network Status Indicator di NoteListScreen (animated banner saat offline)

### Architecture (20%)
- ✅ Clean separation: di/, platform/, data/, viewmodel/, screens/
- ✅ expect/actual pattern untuk semua platform-specific code
- ✅ Koin modules terpisah per layer (common vs platform)

### Code Quality (10%)
- ✅ Clean code tanpa komentar berlebihan
- ✅ MVVM + Repository pattern

### Bonus (+10%)
- ✅ BatteryInfo expect/actual: getBatteryLevel(), isCharging()
- ✅ Android: BatteryManager API
- ✅ JVM: Stub implementation

---

## 📸 Screenshot

### 1. Notes List Screen (dengan Network Status Indicator)
> Screenshot menampilkan daftar notes dengan indicator koneksi internet di bagian atas

### 2. Settings Screen (dengan Device Info + Battery Info)
> Screenshot menampilkan informasi perangkat dan status baterai di halaman Settings

### 3. Network Offline Indicator
> Screenshot saat airplane mode aktif, banner merah "Tidak Ada Koneksi Internet" muncul

---

## 🎥 Demo Video

🔗 **Link Video:** *(tambahkan link video demo 45 detik)*

---

## 🚀 Cara Menjalankan

1. Clone repository
2. Buka project di Android Studio
3. Sync Gradle dependencies
4. Run di Android emulator atau Desktop (JVM)
