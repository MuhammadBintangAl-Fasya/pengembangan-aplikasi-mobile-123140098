## TUGAS 5 — PENGEMBANGAN APLIKASI MOBILE

**Nama:** Muhammad Bintang Alfasya<br>
**NIM:** 123140098<br>
**Kelas:** RA<br>

---

## 📱 Deskripsi Proyek

Aplikasi **MyProfileApp (Notes + Profile)** dibangun berbasis **Kotlin Multiplatform (KMP)** menggunakan kerangka **Compose Multiplatform**. Melalui pengembangan Tugas 5 ini, sistem **Multi-Screen Navigation** diimplementasikan dengan memanfaatkan **Navigation Component** secara menyeluruh guna menciptakan interaksi pengguna yang modern dan dinamis.

Fitur utama pada navigasi meliputi:
- **Bottom Navigation** yang terintegrasi di 3 tab utama (Notes, Favorites, Profile).
- **Navigation Drawer** interaktif (Bonus) dengan usapan layar atau ikon Hamburger.
- **CRUD Operasional Notes** (Create, Read, Update, Delete) lengkap dengan status favorit.
- **Pengiriman Parameter Antar Layar (Argument Passing)** — menggunakan ID catatan/catatan untuk membuka halaman detail.
- **Backstack Behavior** — menjaga konsistensi state ketika pengguna menekan tombol kembali (Back Navigation).

---

## ✨ Rekapitulasi Fitur Aplikasi

| Modul Fitur | Penjelasan Detail |
|---|---|
| 📝 **Catatan & Beranda** | Daftar seluruh catatan aktif; dilengkapi opsi edit, penandaan favorit, dan hapus data secara langsung. |
| ⭐ **Koleksi Favorit** | Representasi catatan yang sudah ditandai. Jika kosong, akan memunculkan status visual _Empty State_. |
| 👤 **Halaman Profil** | Penampilan biodata/profil developer serta tombol aktivasi fitur _Dark Mode_ (Tema Gelap). |
| ➕ **Catat Baru (Add Mode)** | Halaman form mandiri untuk membuat tulisan/entri baru yang dipicu lewat _Floating Action Button_. |
| 📖 **Detail Pembacaan** | Antarmuka khusus untuk membaca seluruh isi catatan tanpa batasan panjang karakter. |
| ✏️ **Edit & Perbarui** | Modul form untuk memanipulasi judul maupun konten _Note_ eksisting melalui Argument ID. |
| 🌙 **Tema Dinamis** | Kontrol manual untuk transisi instan dari penataan warna cerah (Light) ke warna gelap (Dark Mode). |

---

## 🏗️ Struktur Arsitektural Direktori

Demi modularitas, proyek dibagi ke dalam komponen-komponen terpisah berbasis pola desain:

```
composeApp/src/commonMain/kotlin/com/bintang/myprofileapp/
│
├── App.kt                          # Pusat entry aplikasi dan inisiasi Theme/Scaffold
│
├── model/
│   ├── Note.kt                     # Kerangka kelas data untuk Catatan
│   └── ProfileData.kt              # Kerangka kelas data untuk Identitas Profil
│
├── viewmodel/
│   ├── NotesViewModel.kt           # Pusat state & logika penanganan operasi list catatan
│   ├── ProfileViewModel.kt         # Penyimpanan state profil + kontrol Dark Mode
│   └── ProfileUiState.kt           # Struktur antarmuka kondisi (UI State) profil
│
├── navigation/
│   ├── Screen.kt                   # Konsep URL String-based routing per destinasi layar
│   ├── AppNavigation.kt            # Sistem inti NavHost, Tabbar Bawah & fungsi Routing Path
│   └── BottomNavItem.kt            # Metadata masing-masing ikon Tab Navigasi Bawah
│
├── screens/
│   ├── NoteListScreen.kt           # Tampilan Tab 1 (Home/Utama)
│   ├── FavoritesScreen.kt          # Tampilan Tab 2 (Layar Favorit)
│   ├── ProfileScreen.kt            # Tampilan Tab 3 (Layar Profil Developer)
│   ├── NoteDetailScreen.kt         # Halaman perincian catatan -> (Passing Argument: noteId)
│   ├── AddNoteScreen.kt            # Modul pembuatan catatan
│   └── EditNoteScreen.kt           # Modul pembaruan konten catatan -> (Passing Argument: noteId)
│
└── ui/
    ├── components/                 # Modul Antarmuka Reusable (dapat didaur-ulang)
    │   ├── NoteCard.kt             # Desain dasar daftar item catatan
    │   ├── EmptyState.kt           # Modul peringatan jika tabel data kosong
    │   ├── InfoItem.kt             # Form baris informasi
    │   └── ...                     
    └── theme/
        └── AppTheme.kt             # Regulasi skema warna Material Design 3
```

---

## 🗺️ Skema Alur Navigasi (Flow Diagram)

```text
┌─────────────────────────────────────────────────────────┐
│               SISTEM NAVIGATION DRAWER                  │
│                                                         │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐          │
│   │  Notes   │    │Favorites │    │ Profile  │          │
│   │  (Tab 1) │    │ (Tab 2)  │    │ (Tab 3)  │          │
│   └────┬─────┘    └────┬─────┘    └────┬─────┘          │
│        │               │               │                │
└────────┼───────────────┼───────────────┼────────────────┘
         │               │               │
         ▼               │               ▼
   ┌───────────┐         │               │
   │ Add Note  │         │               │
   │  (FAB +)  │         │               │
   └───────────┘         │               │
         │               │               │
         │               │               │
         ▼               ▼               │
   ┌─────────────────────────┐           │
   │      Note Detail        │           │
   │ (Passing Arg: noteId)  │           │
   └───────────┬─────────────┘           │
               │                         │
               ▼                         │
   ┌─────────────────────────┐           │
   │       Edit Note         │           │
   │ (Passing Arg: noteId)  │           │
   └─────────────────────────┘           │
```

### Mekanisme Pengiriman Argumen Routing:
1. **Beranda → Detail:** Setiap kartu disentuh, id dieksekusi melalui URL string (contoh: `note_detail/3`). 
2. **Favorit → Detail:** Sama seperti Beranda, merutekan ID agar aplikasi memperlihatkan layar fokus baca spesifik.
3. **Detail → Edit Note:** Tombol _pencil_ yang ada mengirimkan kembali ID tersebut agar tabel penyuntingan dimuat.
4. **Pembawaan State Mundur:** Aksi simpan dan batal secara implisit menembakkan triggger *popBackStack()* yang menjaga jejak aktivitas agar konsisten seolah seperti tumpukan *stack*.

---

## 📸 Screenshot Setiap Screen

### 1. Notes List Screen (Tab 1 — Home)

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/227890a6-2f46-4ea3-b1b2-c12079ddf7cc) | ![Desktop](https://github.com/user-attachments/assets/0bfdc2be-349e-4c98-98bd-33ef8a711e2d) | 



### 2. Favorites Screen (Tab 2)

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/5f890aef-0f55-49b4-a105-ef818382e5a2) | ![Desktop](https://github.com/user-attachments/assets/291dd523-4545-4311-9b57-2484d27352db) | 



### 3. Profile Screen (Tab 3)

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/89b36574-e9bb-4eb7-8a3e-b14c856f3587) | ![Desktop](https://github.com/user-attachments/assets/bb189d84-a0f9-43dc-8316-b3ebfc0af9fc) | 



### 4. Note Detail Screen

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/ad46ba0f-8dfe-4ec5-90c6-18df663618bc) | ![Desktop](https://github.com/user-attachments/assets/65de23c0-e2cb-4ef2-9c24-427cc2ba1932) | 



### 5. Add Note Screen

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/92928f15-b73f-4415-aafe-3e64a72c4c9a) | ![Desktop](https://github.com/user-attachments/assets/e9ada7dd-b70c-4806-b8fb-15a011dc38e2) | 


### 6. Edit Note Screen

|                                           Android                                           |                                           Desktop                                           | 
|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ![Android](https://github.com/user-attachments/assets/243ea422-8a20-455d-b4d1-2929d0684fd6) | ![Desktop](https://github.com/user-attachments/assets/b855fc2d-8016-4618-ac67-5bd1786bf649) | 


---

## 🎬 Tautan Video Demo Aplikasi

Simak demonstrasi singkat di mana integrasi _Navigation Pattern_ memandu pengiriman state: dari transisi antartab, penambahan, hingga pengiriman argumen sukses diterjemahkan menjadi halaman dinamis.

🔗 **Tonton Demo Singkat:** https://drive.google.com/drive/folders/1wfLP-KvKvb0-4kh8HVueXoE8KaT6Ccx5?usp=sharing

---

## 🛠️ Fondasi Teknologi (Tech Stack)

Integrasi proyek ditopang menggunakan arsitektur modern standar industri multiplatform:
- **Konsep:** Model-View-ViewModel (MVVM)
- **Logika Bahasa:** Kotlin Multiplatform (KMP) `2.3.0`
- **Render UI:** Compose Multiplatform `1.10.0`
- **Metode Transisi:** Navigation Component
- **Pewarnaan Bawaan:** Material Design 3
- **Reactive Stream:** StateFlow & State Variables
