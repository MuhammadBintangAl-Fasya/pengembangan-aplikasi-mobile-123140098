package com.bintang.myprofileapp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.bintang.myprofileapp.db.NotesDatabase
import com.bintang.myprofileapp.db.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class NoteRepository(database: NotesDatabase) {

    private val queries = database.noteQueries

    fun getAllNotes(sortOrder: SortOrder = SortOrder.UPDATED_DESC): Flow<List<Note>> {
        return when (sortOrder) {
            SortOrder.UPDATED_DESC -> queries.selectAll()
            SortOrder.CREATED_ASC -> queries.selectAllByCreatedAsc()
            SortOrder.CREATED_DESC -> queries.selectAllByCreatedDesc()
            SortOrder.TITLE_ASC -> queries.selectAllByTitleAsc()
            SortOrder.TITLE_DESC -> queries.selectAllByTitleDesc()
        }.asFlow().mapToList(Dispatchers.IO)
    }

    fun getNoteByIdFlow(id: Long): Flow<Note?> {
        return queries.selectById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.IO) {
        queries.selectById(id).executeAsOneOrNull()
    }

    suspend fun insertNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.IO) {
            queries.insert(
                title = title,
                content = content,
                is_favorite = 0L,
                created_at = now,
                updated_at = now
            )
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.IO) {
            queries.update(
                title = title,
                content = content,
                updated_at = now,
                id = id
            )
        }
    }

    suspend fun toggleFavorite(id: Long) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.IO) {
            queries.toggleFavorite(updated_at = now, id = id)
        }
    }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        queries.delete(id)
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        val likePattern = "%$query%"
        return queries.search(likePattern, likePattern).asFlow().mapToList(Dispatchers.IO)
    }
}

enum class SortOrder(val displayName: String) {
    UPDATED_DESC("Terbaru Diubah"),
    CREATED_DESC("Terbaru Dibuat"),
    CREATED_ASC("Terlama Dibuat"),
    TITLE_ASC("Judul (A-Z)"),
    TITLE_DESC("Judul (Z-A)")
}
