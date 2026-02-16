package model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class News(
    val id: Int,
    val title: String,
    val category: String,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    // Helper biar formatting tanggal gampang saat di-print
    fun getFormattedTime(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}