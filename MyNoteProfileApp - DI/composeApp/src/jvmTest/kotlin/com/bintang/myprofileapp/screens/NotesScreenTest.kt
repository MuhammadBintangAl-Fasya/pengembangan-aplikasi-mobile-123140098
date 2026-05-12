package com.bintang.myprofileapp.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.bintang.myprofileapp.model.NoteUi
import com.bintang.myprofileapp.platform.NetworkMonitor
import com.bintang.myprofileapp.ui.TestTags
import com.bintang.myprofileapp.viewmodel.NotesUiState
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class NotesScreenTest {

    private val sampleNotes = listOf(
        NoteUi(id = 1L, title = "First Note", content = "Content 1", isFavorite = false, createdAt = 1000L, updatedAt = 2000L),
        NoteUi(id = 2L, title = "Second Note", content = "Content 2", isFavorite = true, createdAt = 1500L, updatedAt = 2500L)
    )

    private val testModule = module {
        single { NetworkMonitor() }
    }

    @Test
    fun loadingState_showsLoadingIndicator() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                MaterialTheme {
                    NoteListScreen(
                        uiState = NotesUiState.Loading,
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onClearSearch = {},
                        onNoteClick = {},
                        onAddClick = {},
                        onFavoriteClick = {},
                        onMenuClick = {}
                    )
                }
            }
        }

        onNodeWithTag(TestTags.LOADING_STATE).assertIsDisplayed()
    }

    @Test
    fun emptyState_showsEmptyMessage() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                MaterialTheme {
                    NoteListScreen(
                        uiState = NotesUiState.Empty,
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onClearSearch = {},
                        onNoteClick = {},
                        onAddClick = {},
                        onFavoriteClick = {},
                        onMenuClick = {}
                    )
                }
            }
        }

        onNodeWithTag(TestTags.EMPTY_STATE).assertIsDisplayed()
        onNodeWithText("Belum Ada Notes").assertIsDisplayed()
    }

    @Test
    fun contentState_showsNotesList() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                MaterialTheme {
                    NoteListScreen(
                        uiState = NotesUiState.Content(sampleNotes),
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onClearSearch = {},
                        onNoteClick = {},
                        onAddClick = {},
                        onFavoriteClick = {},
                        onMenuClick = {}
                    )
                }
            }
        }

        onNodeWithTag(TestTags.NOTES_LIST).assertIsDisplayed()
        onNodeWithText("First Note").assertIsDisplayed()
        onNodeWithText("Second Note").assertIsDisplayed()
    }

    @Test
    fun addButton_isDisplayed() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                MaterialTheme {
                    NoteListScreen(
                        uiState = NotesUiState.Empty,
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onClearSearch = {},
                        onNoteClick = {},
                        onAddClick = {},
                        onFavoriteClick = {},
                        onMenuClick = {}
                    )
                }
            }
        }

        onNodeWithTag(TestTags.ADD_BUTTON).assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessage() = runComposeUiTest {
        setContent {
            KoinApplication(application = { modules(testModule) }) {
                MaterialTheme {
                    NoteListScreen(
                        uiState = NotesUiState.Error("Terjadi kesalahan"),
                        searchQuery = "",
                        onSearchQueryChange = {},
                        onClearSearch = {},
                        onNoteClick = {},
                        onAddClick = {},
                        onFavoriteClick = {},
                        onMenuClick = {}
                    )
                }
            }
        }

        onNodeWithTag(TestTags.ERROR_STATE).assertIsDisplayed()
        onNodeWithText("Terjadi kesalahan").assertIsDisplayed()
    }
}
