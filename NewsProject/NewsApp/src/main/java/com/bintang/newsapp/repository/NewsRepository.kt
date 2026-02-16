package repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import model.News
import java.time.LocalDateTime

class NewsRepository {

    // Data Pool (Sesuai referensi kamu)
    private val newsPool = listOf(
        News(1, "Kotlin 2.0 Resmi Dirilis", "Teknologi", "Kotlin 2.0 hadir dengan compiler K2."),
        News(2, "Indonesia Menang Piala AFF", "Olahraga", "Timnas menang 3-1 lawan Thailand."),
        News(3, "Harga Emas Naik Tajam", "Ekonomi", "Emas melonjak ke USD 2.800."),
        News(4, "Film Box Office Baru", "Hiburan", "Tembus 5 juta penonton."),
        News(5, "Gempa M6.2 Sulawesi", "Nasional", "BMKG catat gempa di Sulteng."),
        News(6, "Madrid vs Bayern Imbang", "Olahraga", "Skor akhir 2-2 di Liga Champions."),
        News(7, "Rupiah Menguat", "Ekonomi", "Kurs Rp15.200 per USD."),
        News(8, "Konser We The Fest", "Hiburan", "Lineup fase pertama diumumkan.")
    )

    // [FITUR 1] Flow Emit berita tiap 2 detik
    fun getNewsStream(): Flow<News> = flow {
        var index = 0
        while (true) {
            delay(2000)
            // Ambil data secara berulang (looping list) & update timestamp biar fresh
            val rawNews = newsPool[index % newsPool.size]
            val freshNews = rawNews.copy(timestamp = LocalDateTime.now())

            emit(freshNews)
            index++
        }
    }

    // [FITUR 5] Simulasi Fetch Detail (Async logic source)
    suspend fun getNewsDetail(id: Int): News? {
        delay(1000) // Simulasi network delay
        return newsPool.find { it.id == id }
    }
}