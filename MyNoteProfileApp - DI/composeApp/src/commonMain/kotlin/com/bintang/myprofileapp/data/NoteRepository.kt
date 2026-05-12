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

interface NoteRepository {
    fun getAllNotes(sortOrder: SortOrder = SortOrder.UPDATED_DESC): Flow<List<Note>>
    fun getNoteByIdFlow(id: Long): Flow<Note?>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(title: String, content: String)
    suspend fun updateNote(id: Long, title: String, content: String)
    suspend fun toggleFavorite(id: Long)
    suspend fun deleteNote(id: Long)
    fun searchNotes(query: String): Flow<List<Note>>
}

class NoteRepositoryImpl(
    database: NotesDatabase,
    private val timeProvider: () -> Long = { Clock.System.now().toEpochMilliseconds() }
) : NoteRepository {

    private val queries = database.noteQueries

    override fun getAllNotes(sortOrder: SortOrder): Flow<List<Note>> {
        return when (sortOrder) {
            SortOrder.UPDATED_DESC -> queries.selectAll()
            SortOrder.CREATED_ASC -> queries.selectAllByCreatedAsc()
            SortOrder.CREATED_DESC -> queries.selectAllByCreatedDesc()
            SortOrder.TITLE_ASC -> queries.selectAllByTitleAsc()
            SortOrder.TITLE_DESC -> queries.selectAllByTitleDesc()
        }.asFlow().mapToList(Dispatchers.IO)
    }

    override fun getNoteByIdFlow(id: Long): Flow<Note?> {
        return queries.selectById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    override suspend fun getNoteById(id: Long): Note? = withContext(Dispatchers.IO) {
        queries.selectById(id).executeAsOneOrNull()
    }

    override suspend fun insertNote(title: String, content: String) {
        val now = timeProvider()
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

    override suspend fun updateNote(id: Long, title: String, content: String) {
        val now = timeProvider()
        withContext(Dispatchers.IO) {
            queries.update(
                title = title,
                content = content,
                updated_at = now,
                id = id
            )
        }
    }

    override suspend fun toggleFavorite(id: Long) {
        val now = timeProvider()
        withContext(Dispatchers.IO) {
            queries.toggleFavorite(updated_at = now, id = id)
        }
    }

    override suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        queries.delete(id)
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
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
