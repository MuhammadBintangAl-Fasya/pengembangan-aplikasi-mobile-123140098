package com.bintang.myprofileapp.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class NoteUi(
    val id: Long,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
) {
    val formattedTimestamp: String
        get() = formatTimestamp(updatedAt)
}

fun formatTimestamp(epochMillis: Long): String {
    if (epochMillis == 0L) return ""
    return try {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val months = listOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val month = months[dateTime.monthNumber - 1]
        val day = dateTime.dayOfMonth
        val year = dateTime.year
        val hour = dateTime.hour.toString().padStart(2, '0')
        val minute = dateTime.minute.toString().padStart(2, '0')
        "$day $month $year, $hour:$minute"
    } catch (_: Throwable) {
        ""
    }
}

fun com.bintang.myprofileapp.db.Note.toUi(): NoteUi {
    return NoteUi(
        id = this.id,
        title = this.title,
        content = this.content,
        isFavorite = this.is_favorite == 1L,
        createdAt = this.created_at,
        updatedAt = this.updated_at
    )
}
