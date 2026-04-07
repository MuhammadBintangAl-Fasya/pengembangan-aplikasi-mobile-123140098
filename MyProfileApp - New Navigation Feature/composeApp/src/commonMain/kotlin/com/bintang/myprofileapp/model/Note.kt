package com.bintang.myprofileapp.model

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val timestamp: String,
    val isFavorite: Boolean = false
)

val sampleNotes = listOf(
    Note(
        id = 1,
        title = "Mobile Development Progress",
        content = "Hari ini fokus mengerjakan tugas Mobile Development.\n\n" +
                "Sudah mulai paham alur navigation di Jetpack Compose, " +
                "terutama penggunaan NavHost dan NavController.\n\n" +
                "Yang masih perlu diperbaiki:\n" +
                "• Routing antar screen\n" +
                "• Back navigation\n" +
                "• UI supaya lebih rapi\n\n" +
                "Targetnya aplikasi notes bisa berjalan tanpa error.",
        timestamp = "7 Apr 2026, 20:10",
        isFavorite = true
    ),

    Note(
        id = 2,
        title = "Belajar Kotlin",
        content = "Belajar Kotlin lebih dalam lagi.\n\n" +
                "Materi yang dipelajari:\n" +
                "• Data class\n" +
                "• Function\n" +
                "• List dan collection\n" +
                "• Coroutine dasar\n\n" +
                "Kotlin cukup enak dipakai karena sintaksnya sederhana.\n" +
                "Harus lebih sering latihan supaya terbiasa.",
        timestamp = "6 Apr 2026, 14:20",
        isFavorite = true
    ),

    Note(
        id = 3,
        title = "Latihan CTF",
        content = "Mulai latihan CTF lagi minggu ini.\n\n" +
                "Fokus latihan:\n" +
                "• Web exploitation\n" +
                "• Cryptography\n" +
                "• Basic reverse engineering\n\n" +
                "Platform yang dipakai:\n" +
                "picoCTF dan TryHackMe.\n\n" +
                "Target: minimal solve beberapa challenge setiap minggu.",
        timestamp = "5 Apr 2026, 09:00",
        isFavorite = true
    ),

    Note(
        id = 4,
        title = "Linux Command",
        content = "Command Linux yang sering dipakai:\n\n" +
                "ls - melihat file\n" +
                "cd - pindah folder\n" +
                "grep - mencari kata\n" +
                "chmod - ubah permission\n" +
                "file - cek tipe file\n" +
                "strings - melihat isi file\n\n" +
                "Harus lebih sering latihan di Kali Linux supaya terbiasa.",
        timestamp = "4 Apr 2026, 20:30",
        isFavorite = false
    ),

    Note(
        id = 5,
        title = "Target Belajar",
        content = "Target belajar semester ini:\n\n" +
                "• Konsisten belajar Mobile Development\n" +
                "• Lebih paham Kotlin\n" +
                "• Rutin latihan CTF\n" +
                "• Menguasai Linux dasar\n" +
                "• Aktif membuat project\n\n" +
                "Fokus utama adalah konsistensi belajar setiap hari.",
        timestamp = "3 Apr 2026, 18:00",
        isFavorite = true
    )
)
