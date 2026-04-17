# 📝 Notes App — Tugas 7

**Tugas 7 - Pengembangan Aplikasi Mobile**  
**Nama:** Muhammad Bintang Alfasya  
**NIM:** 123140098  
**Kelas:** RA

---

## 📋 Deskripsi

Aplikasi **Notes & Profile** berbasis **Kotlin Multiplatform (KMP)** dengan kerangka **Compose Multiplatform**. Fitur utama dalam versi ini meliputi:

- **SQLDelight** database untuk penyimpanan lokal (offline-first)
- **CRUD Operations** — Create, Read, Update, Delete untuk manajemen catatan
- **Search** — Pencarian catatan asinkronus berdasarkan judul atau konten
- **Settings** — Manajemen UI Tema (Light/Dark mode) & Urutan catatan melalui library DataStore (Multiplatform Settings)
- **Favorites** — Penandaan interaktif catatan sebagai prioritas/favorit
- **UI States** — Representasi progres melalui state Loading, Empty state, & Content list view

---

## 💾 Database Schema

### Tabel: `Note`

```sql
CREATE TABLE Note (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  title       TEXT    NOT NULL,
  content     TEXT    NOT NULL,
  is_favorite INTEGER NOT NULL DEFAULT 0,
  created_at  INTEGER NOT NULL,
  updated_at  INTEGER NOT NULL
);
```

### Queries SQLDelight (.sq)

| Query | Deskripsi |
|-------|-----------|
| `selectAll` | Ambil semua catatan umum, diurutkan `updated_at DESC` |
| `selectAllByCreatedAsc` | Ambil semua catatan, diurutkan waktu dibuat (`created_at ASC`) |
| `selectAllByCreatedDesc` | Ambil semua catatan, diurutkan waktu dibuat (`created_at DESC`) |
| `selectAllByTitleAsc` | Ambil semua catatan, diurutkan menurut abjad (`title ASC`) |
| `selectAllByTitleDesc` | Ambil semua catatan, diurutkan abjad kebalikan (`title DESC`) |
| `selectById` | Ambil data satu catatan secara utuh berdasarkan argumen ID |
| `insert` | Mengeksekusi penambahan catatan baru ke _database_ lokal |
| `update` | Update rincian konten, judul, dan waktu perbaruan catatan terkait |
| `toggleFavorite` | Mengubah *toggle* status favorit interaktif (0 → 1 atau 1 → 0) |
| `delete` | Menghancurkan sebaris identitas catatan berdasarkan ID |
| `search` | Meneliti kecocokan judul/isi lewat param SQL (`LIKE`) |

---

## 🏗️ Arsitektur & Pattern

Aplikasi diatur rapi berpedoman pada **MVVM + Repository Pattern** lengkap dengan **offline-first** arsitektur lokal:

```text
UI Layer (Compose Screens via NavHost)
    ↓
ViewModel Layer (StateFlow + UI Data States)
    ↓
Repository Layer (NoteRepository & SettingsManager)
    ↓
Data Layer (SQLDelight Database + Multiplatform Settings)
```

### Struktur File

```
composeApp/src/commonMain/kotlin/com/bintang/myprofileapp/
├── App.kt                          # Pusat entry aplikasi dan root basis tema
├── data/
│   ├── NoteRepository.kt           # Repository penengah aksi CRUD + Search SQLDelight
│   └── SettingsManager.kt          # Multiplatform Settings: penanganan tema & penyortiran list
├── db/
│   └── DatabaseDriverFactory.kt    # Interaksi pabrik (expect/actual) driver platform SQLDelight
├── model/
│   ├── Note.kt                     # Kelas data standar entitas sistem Notes
│   └── ProfileData.kt              # Kelas data abstrak bagi profil bio developer
├── viewmodel/
│   ├── NotesViewModel.kt           # ViewModel UI logika: reaktivitas CRUD, Search, dan Favorit
│   ├── SettingsViewModel.kt        # ViewModel Pengaturan: Akses baca rubah state Light/Dark
│   └── ProfileViewModel.kt         # Pengekspos data StateFlow screen bagian profil 
├── navigation/
│   ├── Screen.kt                   # Rute definisi berbasis argument string sealed class
│   ├── AppNavigation.kt            # Kumpulan instruksi kerangka utama `NavHost`
│   └── BottomNavItem.kt            # Indeks model identitas menu *Bottom Navigation*
├── screens/
│   ├── NoteListScreen.kt           # Tab 1: Daftar Notes (Home) utama + Search bar kolum
│   ├── FavoritesScreen.kt          # Tab 2: Modul penyaring catatan khusus berbintang kuning
│   ├── ProfileScreen.kt            # Tab 3: Menghidangkan layar Profile data mahasiswa
│   ├── NoteDetailScreen.kt         # Area muat bacaan penuh teks berjalan -> (argumen: noteId)
│   ├── AddNoteScreen.kt            # Canvas *form* pendaftaran catatan baru (kosong)
│   ├── EditNoteScreen.kt           # Canvas mutasi dan adaptasi Note lamamu -> (argumen: noteId)
│   └── SettingsScreen.kt           # Pusat kendali pengaturan preferensi UX
└── ui/
    └── components/                 # Abstraksi reusabilitas views
        ├── NoteCard.kt             # View elemen list item pembentuk lazycolumn
        └── EmptyState.kt           # Tampilan visual kosong khusus
```

---

## 🛠️ Konfigurasi Tech Stack

| Teknologi | Kegunaan |
|-----------|----------|
| Kotlin Multiplatform | Berbagi logika murni antar sistem (Android, iOS, dan Desktop) |
| Compose Multiplatform | Pustaka kerangka penyediaan *UI Declarative* multi OS |
| SQLDelight | Eksekutor *local database* stabil dalam basis _type-safe queries_ |
| Multiplatform Settings | Persistensi parameter seperti preferensi sistem DataStore pengganti |
| Navigation Compose | Rutinitas pergerakan UI _pop and track stack_ aman argumen |
| Material 3 | Panduan desain estetis palet modern terintegrasi otomatis |
| ViewModel / StateFlow | Mengatur daur hidup rotasi state terkurung tanpa memori bocor |

---

## ✨ Fitur Tersedia

### SQLDelight Setup (20%)
- ✅ Tabel `Note` disesuaikan tepat konfigurasi pada penanganan 6 entitas kolom esensial.
- ✅ Sebanyak 11 tipe query fungsional dipisah berdasarkan tujuannya masing-masing dalam file `.sq`.
- ✅ Pengkondisian `expect/actual` siap tanggap bagi arsitektur kompilasi target terhubung.

### CRUD Operations (25%)
- ✅ **Create** — Tuangkan curhatan *offline* melalui `AddNoteScreen`.
- ✅ **Read** — Suguhan penayangan catatan secara kolektif mapun mendalam menggunakan `NoteDetailScreen`.
- ✅ **Update** — Mampu merestorasi penamaan dan deskripsi Note usang.
- ✅ **Delete** — Mekanisme penghancuran baris tabel permanen secara instan.

### Settings / DataStore (15%)
- ✅ **Pilihan Tema** — Menyesuaikan *Dark, Light,* dan *System Default* secara langsung dari Settings.
- ✅ **Urutan Penampil** — Menerapkan pemilahan daftar data Notes *by date updated* atau abjad Alfabetik.
- ✅ **Memori Abadi** — Setiap transisi ditanamkan melalui *key-storage multiplatform-settings*.

### Search Feature (15%)
- ✅ Tersedianya kolom TextField responsif dalam `NoteListScreen`.
- ✅ Pindaian mendalam *case-insensitive* ke entitas `title` serta `content` menaungi operator SQL `LIKE`.
- ✅ *Empty state* khusus melarang penampakan rumpang saat tidak ada hasil pencarian terpaut.

### UI/UX (15%)
- ✅ **Desain Negara (UI States)** — Penyelarasan mutlak transisi antara loading _Circular Progress_, *Empty Box*, dan muatan aslinya.
- ✅ Sistem navigasi tab bawah untuk penelusuran perpindahan instan namun persisten.

### Code Quality (10%)
- ✅ Adopsi pola Repository-Driven Model-View-ViewModel yang matang.
- ✅ Isolasi file ketat ke folder dan paket yang relevan (`data/`, `viewmodel/`, `ui/`).

---

## 📸 Antarmuka Screenshot

### 1. Notes List Screen (beserta komponen Search)
![NotesListScreen](https://github.com/user-attachments/assets/ee501a2a-1417-49c7-8545-878619ccc660)

### 2. Favorites Screen
![FavoritesScreen](https://github.com/user-attachments/assets/8d5a0ed7-4aad-4409-b6ff-7f49856f9f77)

### 3. Add Note Screen
![AddNoteScreen](https://github.com/user-attachments/assets/2cb21000-2ce5-46e4-9193-c795570b47d2)

### 4. Detail Note Screen
![NoteDetailScreen](https://github.com/user-attachments/assets/aef23f09-e4a5-4dc0-8dd6-4c620866246d)

### 5. Profile Screen
![ProfileScreen](https://github.com/user-attachments/assets/d6cbedfb-2e1d-4050-ac76-d03d58c2e8ba)

### 6. Edit Screen
![EditScreen](https://github.com/user-attachments/assets/f7bf279f-9c67-45e9-8128-3d137a3455c5)

### 7. Setting Screen
![SettingScreen](https://github.com/user-attachments/assets/5c4a05f9-7806-4ebb-9897-4492afd3cab7)

## 🎥 Panduan Demo Video (Tugas 7)

Tonton demonstrasi utuh bagaimana fungsionalitas disematkan secara native.
Dalam pemutar anda akan melihat cara *offline mode* CRUD beroperasi, filter tipe penyortir dari *Settings*, serta interaktivitas fitur *Favorite/Pinned Note*.

🔗 **Lampiran Tautan Video:** ![Video](https://drive.google.com/file/d/1ceywcubTLx_St47YhpKVhY5uNDzqr280/view?usp=drive_link)

---

## 🚀 Fase Penjalanan Lokal

1. Persiapkan ruang simpan serta `git clone`.
2. Buka dan delegasikan project ini pada Android Studio (*saran: versi update Koala+*).
3. Pastikan eksekusi perizinan dependensi membuahkan *BUILD SUCCESSFUL*.
4. *SQLDelight interface* akan menghasilkan kelas objeknya; segera panggil dan *Run* menggunakan emulator Android target pilihan.
