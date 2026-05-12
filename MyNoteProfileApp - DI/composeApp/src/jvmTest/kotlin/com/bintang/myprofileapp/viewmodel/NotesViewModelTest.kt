package com.bintang.myprofileapp.viewmodel

import app.cash.turbine.test
import com.bintang.myprofileapp.data.NoteRepository
import com.bintang.myprofileapp.data.SettingsManager
import com.bintang.myprofileapp.data.SortOrder
import com.bintang.myprofileapp.db.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val mockRepository = mockk<NoteRepository>(relaxed = true)
    private val mockSettingsManager = mockk<SettingsManager>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private val sampleNote = Note(
        id = 1L,
        title = "Test Note",
        content = "Test Content",
        is_favorite = 0L,
        created_at = 1000L,
        updated_at = 2000L
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockSettingsManager.sortOrderFlow } returns MutableStateFlow(SortOrder.UPDATED_DESC)
        every { mockSettingsManager.sortOrder } returns SortOrder.UPDATED_DESC
        coEvery { mockRepository.getAllNotes(any()) } returns flowOf(listOf(sampleNote))
        coEvery { mockRepository.searchNotes(any()) } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): NotesViewModel {
        return NotesViewModel(mockRepository, mockSettingsManager)
    }

    @Test
    fun `initial uiState is Loading`() = runTest {
        val viewModel = createViewModel()
        val initialState = viewModel.uiState.value
        assertIs<NotesUiState.Loading>(initialState)
    }

    @Test
    fun `uiState emits Content when notes available`() = runTest {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            assertIs<NotesUiState.Content>(state)
            assertEquals(1, state.notes.size)
            assertEquals("Test Note", state.notes[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Empty when no notes`() = runTest {
        coEvery { mockRepository.getAllNotes(any()) } returns flowOf(emptyList())

        val viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1)
            val state = awaitItem()
            assertIs<NotesUiState.Empty>(state)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addNote calls repository insertNote`() = runTest {
        coEvery { mockRepository.insertNote(any(), any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.addNote("New Note", "New Content")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepository.insertNote("New Note", "New Content") }
    }

    @Test
    fun `deleteNote calls repository deleteNote`() = runTest {
        coEvery { mockRepository.deleteNote(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.deleteNote(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepository.deleteNote(1L) }
    }

    @Test
    fun `toggleFavorite calls repository toggleFavorite`() = runTest {
        coEvery { mockRepository.toggleFavorite(any()) } returns Unit
        val viewModel = createViewModel()

        viewModel.toggleFavorite(1L)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { mockRepository.toggleFavorite(1L) }
    }

    @Test
    fun `getNoteById returns mapped NoteUi`() = runTest {
        coEvery { mockRepository.getNoteById(1L) } returns sampleNote

        val viewModel = createViewModel()
        val result = viewModel.getNoteById(1L)

        assertNotNull(result)
        assertEquals("Test Note", result.title)
        assertEquals("Test Content", result.content)
    }

    @Test
    fun `getNoteById returns null for non-existent note`() = runTest {
        coEvery { mockRepository.getNoteById(999L) } returns null

        val viewModel = createViewModel()
        val result = viewModel.getNoteById(999L)

        assertNull(result)
    }

    @Test
    fun `onSearchQueryChange updates searchQuery`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSearchQueryChange("test query")

        assertEquals("test query", viewModel.searchQuery.value)
    }

    @Test
    fun `clearSearch resets searchQuery to empty`() = runTest {
        val viewModel = createViewModel()

        viewModel.onSearchQueryChange("something")
        viewModel.clearSearch()

        assertEquals("", viewModel.searchQuery.value)
    }
}
