package com.bintang.myprofileapp.data

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import com.bintang.myprofileapp.db.NotesDatabase
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NoteRepositoryTest {

    private lateinit var database: NotesDatabase
    private lateinit var repository: NoteRepositoryImpl

    @BeforeTest
    fun setup() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        NotesDatabase.Schema.create(driver)
        database = NotesDatabase(driver)
        repository = NoteRepositoryImpl(database) { System.currentTimeMillis() }
    }

    @Test
    fun `insertNote and getAllNotes returns inserted note`() = runTest {
        repository.insertNote("Test Title", "Test Content")

        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("Test Title", notes[0].title)
            assertEquals("Test Content", notes[0].content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getNoteById returns correct note`() = runTest {
        repository.insertNote("Find Me", "Content Here")

        val allNotes = database.noteQueries.selectAll().executeAsList()
        val insertedId = allNotes.first().id

        val note = repository.getNoteById(insertedId)
        assertNotNull(note)
        assertEquals("Find Me", note.title)
        assertEquals("Content Here", note.content)
    }

    @Test
    fun `getNoteById returns null for non-existent id`() = runTest {
        val note = repository.getNoteById(999L)
        assertNull(note)
    }

    @Test
    fun `updateNote updates title and content`() = runTest {
        repository.insertNote("Old Title", "Old Content")
        val id = database.noteQueries.selectAll().executeAsList().first().id

        repository.updateNote(id, "New Title", "New Content")

        val updated = repository.getNoteById(id)
        assertNotNull(updated)
        assertEquals("New Title", updated.title)
        assertEquals("New Content", updated.content)
    }

    @Test
    fun `deleteNote removes note from database`() = runTest {
        repository.insertNote("To Delete", "Content")
        val id = database.noteQueries.selectAll().executeAsList().first().id

        repository.deleteNote(id)

        val deleted = repository.getNoteById(id)
        assertNull(deleted)
    }

    @Test
    fun `toggleFavorite changes favorite status`() = runTest {
        repository.insertNote("Fav Note", "Content")
        val id = database.noteQueries.selectAll().executeAsList().first().id

        assertEquals(0L, repository.getNoteById(id)!!.is_favorite)

        repository.toggleFavorite(id)
        assertEquals(1L, repository.getNoteById(id)!!.is_favorite)

        repository.toggleFavorite(id)
        assertEquals(0L, repository.getNoteById(id)!!.is_favorite)
    }

    @Test
    fun `searchNotes finds matching notes by title`() = runTest {
        repository.insertNote("Kotlin Guide", "Learn Kotlin")
        repository.insertNote("Java Guide", "Learn Java")
        repository.insertNote("Python Tips", "Tips and tricks")

        repository.searchNotes("Guide").test {
            val results = awaitItem()
            assertEquals(2, results.size)
            assertTrue(results.all { it.title.contains("Guide") })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchNotes finds matching notes by content`() = runTest {
        repository.insertNote("Note 1", "Contains kotlin keyword")
        repository.insertNote("Note 2", "Contains java keyword")

        repository.searchNotes("kotlin").test {
            val results = awaitItem()
            assertEquals(1, results.size)
            assertEquals("Note 1", results[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAllNotes with different sort orders`() = runTest {
        repository.insertNote("Banana", "Content B")
        repository.insertNote("Apple", "Content A")
        repository.insertNote("Cherry", "Content C")

        repository.getAllNotes(SortOrder.TITLE_ASC).test {
            val notes = awaitItem()
            assertEquals("Apple", notes[0].title)
            assertEquals("Banana", notes[1].title)
            assertEquals("Cherry", notes[2].title)
            cancelAndIgnoreRemainingEvents()
        }

        repository.getAllNotes(SortOrder.TITLE_DESC).test {
            val notes = awaitItem()
            assertEquals("Cherry", notes[0].title)
            assertEquals("Banana", notes[1].title)
            assertEquals("Apple", notes[2].title)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
