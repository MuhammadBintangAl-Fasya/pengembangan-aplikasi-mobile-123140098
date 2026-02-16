# News Feed Simulator - Tugas 2 Pemrograman Aplikasi Mobile

**Nama:** Muhammad Bintang Al-Fasya  
**NIM:** 123140098  
**Instansi:** Institut Teknologi Sumatera (ITERA)

Aplikasi konsol Kotlin interaktif yang mensimulasikan aliran berita (news feed) secara real-time. Proyek ini menerapkan prinsip **Clean Architecture** serta memanfaatkan fitur unggulan Kotlin seperti **Flow**, **StateFlow**, dan **Coroutines**.

---

## 🚀 Fitur Utama

| No | Fitur | Deskripsi Teknis |
|----|-------|-------------|
| 1 | **Infinite Stream** | Menggunakan `Flow` builder untuk melakukan `emit` berita secara prosedural dan terus-menerus setiap 2 detik. |
| 2 | **Reactive Filtering** | Implementasi `flatMapLatest` untuk mengganti saluran berita (Technology, Sports, Economy, dsb) secara instan tanpa tumpang tindih. |
| 3 | **Data Transformation** | Penggunaan operator `.map` untuk memformat data mentah menjadi tampilan yang informatif di konsol. |
| 4 | **State Management** | `StateFlow` digunakan sebagai *single source of truth* untuk memantau jumlah bacaan dan referensi berita terbaru secara reaktif. |
| 5 | **Asynchronous Detail** | Menggunakan `withContext(Dispatchers.IO)` untuk simulasi pengambilan detail berita tanpa memblokir aliran utama (*non-blocking*). |

---

## 📂 Struktur Proyek (Clean Code)

Proyek ini dipisahkan menjadi beberapa layer untuk menjaga skalabilitas dan pemisahan tanggung jawab (Separation of Concerns):



Struktur folder pada modul **NewsApp**:
```text
NewsApp/src/main/java/com.bintang.newsapp/
├── manager/
│    └── NewsManager.kt      # Business Logic & State Holder (Flow Operators)
├── model/
│    └── News.kt             # Data Entity (Plain Old Kotlin Object)
├── repository/
│    └── NewsRepository.kt   # Data Provider (Infinite Stream Producer)
└── Main.kt                  # App Entry Point & CLI Interaction


## Cara Menjalankan

### Prasyarat

- **JDK 17** atau lebih baru
- Terminal / Command Prompt

### Langkah

1. **Buka folder proyek**

   ```bash
   cd /path/to/NewsProject/NewsApp
   ```

2. **Jalankan aplikasi**

   ```bash
   ./gradlew run --console=plain
   ```

   > Di Windows: `gradlew.bat run --console=plain`

3. **Gunakan menu interaktif** — ketik pilihan lalu Enter:

   | Input | Aksi                         |
      |-------|------------------------------|
   | `1`   | Tampilkan semua berita       |
   | `2`   | Filter hanya Teknologi       |
   | `3`   | Filter hanya Olahraga        |
   | `4`   | Filter hanya Ekonomi         |
   | `5`   | Filter hanya Hiburan         |
   | `6`   | Filter hanya Nasional        |
   | `r`   | Baca detail 3 berita (async) |
   | `m`   | untuk melihat menu kembali   |
   | `q`   | Keluar & tampilkan ringkasan |

## Contoh Output

```
==============================================
       NEWS TERMINAL CLIENT v1.0
==============================================
PILIH SALURAN:
(1) Semua       (2) Teknologi   (3) Olahraga
(4) Ekonomi     (r) Baca Detail (m) Lihat Menu
(q) Keluar
----------------------------------------------

[INPUT] > Menghubungkan ke satelit [Topik: All]...
 >> [00:43:03] [TEKNOLOGI] Kotlin 2.0 Resmi Dirilis
 >> [00:43:05] [OLAHRAGA] Indonesia Menang Piala AFF
 >> [00:43:07] [EKONOMI] Harga Emas Naik Tajam
r

[INPUT] > 
Mengunduh konten lengkap (ID: 3)...

+----------------------------------------------------------+
|  NEWS CONTENT DETAIL
+----------------------------------------------------------+
| ID       : 3
| JUDUL    : Harga Emas Naik Tajam
| KATEGORI : Ekonomi
| WAKTU    : 00:43:01
+----------------------------------------------------------+
| ISI BERITA:
| Emas melonjak ke USD 2.800.                              |
+----------------------------------------------------------+
Statistik: 1 berita telah selesai dibaca.

 >> [00:43:09] [HIBURAN] Film Box Office Baru
 >> [00:43:11] [NASIONAL] Gempa M6.2 Sulawesi
 >> [00:43:13] [OLAHRAGA] Madrid vs Bayern Imbang
4

=============== FILTER AKTIF: EKONOMI ===============

[INPUT] > Menghubungkan ke satelit [Topik: Ekonomi]...
 >> [00:43:20] [EKONOMI] Harga Emas Naik Tajam
m
PILIH SALURAN:
(1) Semua       (2) Teknologi   (3) Olahraga
(4) Ekonomi     (r) Baca Detail (m) Lihat Menu
(q) Keluar
----------------------------------------------

[INPUT] >  >> [00:43:28] [EKONOMI] Rupiah Menguat
q

Mematikan sistem...
Statistik Akhir: 1 Berita dibaca.
Sampai jumpa!
```