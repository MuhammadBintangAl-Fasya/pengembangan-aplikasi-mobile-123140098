package com.bintang.myprofileapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bintang.myprofileapp.model.NoteUi
import com.bintang.myprofileapp.platform.NetworkMonitor
import com.bintang.myprofileapp.ui.components.EmptyState
import com.bintang.myprofileapp.ui.components.ErrorState
import com.bintang.myprofileapp.ui.components.LoadingState
import com.bintang.myprofileapp.ui.components.NoteCard
import com.bintang.myprofileapp.viewmodel.NotesUiState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    uiState: NotesUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onMenuClick: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onClose = {
                        isSearchActive = false
                        onClearSearch()
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            "My Notes",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onMenuClick) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Notes"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NetworkStatusIndicator()

            when (uiState) {
                is NotesUiState.Loading -> {
                    LoadingState(
                        message = "Memuat notes..."
                    )
                }

                is NotesUiState.Empty -> {
                    EmptyState(
                        icon = Icons.Default.NoteAlt,
                        title = "Belum Ada Notes",
                        subtitle = "Tap tombol + untuk membuat note pertamamu"
                    )
                }

                is NotesUiState.Content -> {
                    if (uiState.notes.isEmpty() && searchQuery.isNotBlank()) {
                        EmptyState(
                            icon = Icons.Default.SearchOff,
                            title = "Tidak Ditemukan",
                            subtitle = "Tidak ada notes yang cocok dengan \"$searchQuery\""
                        )
                    } else {
                        NotesList(
                            notes = uiState.notes,
                            onNoteClick = onNoteClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }

                is NotesUiState.Error -> {
                    ErrorState(
                        message = uiState.message
                    )
                }
            }
        }
    }
}

@Composable
fun NetworkStatusIndicator() {
    val networkMonitor: NetworkMonitor = koinInject()
    val isConnected by networkMonitor.observeConnectivity()
        .collectAsState(initial = true)

    AnimatedVisibility(
        visible = !isConnected,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Tidak Ada Koneksi Internet",
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Cari notes...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        },
        actions = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close search"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun NotesList(
    notes: List<NoteUi>,
    onNoteClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(
            items = notes,
            key = { _, note -> note.id }
        ) { index, note ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = index * 50
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        delayMillis = index * 50
                    ),
                    initialOffsetY = { it / 2 }
                )
            ) {
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note.id) },
                    onFavoriteClick = { onFavoriteClick(note.id) }
                )
            }
        }
    }
}
