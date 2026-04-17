package com.bintang.myprofileapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.bintang.myprofileapp.model.NoteUi
import com.bintang.myprofileapp.screens.*
import com.bintang.myprofileapp.viewmodel.NotesViewModel
import com.bintang.myprofileapp.viewmodel.ProfileUiState
import com.bintang.myprofileapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

/**
 * MainScreen — root composable yang mengatur navigasi seluruh aplikasi.
 * Menggunakan ModalNavigationDrawer + BottomNavigationBar + NavHost.
 *
 * Dependencies yang diterima:
 * - notesViewModel: mengelola CRUD notes dengan SQLDelight
 * - settingsViewModel: mengelola tema dan sort order (multiplatform-settings)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    profileUiState: ProfileUiState,
    onToggleDarkMode: () -> Unit,
    onToggleEditing: () -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    notesViewModel: NotesViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()

    // Collect notes UI state
    val notesUiState by notesViewModel.uiState.collectAsState()
    val searchQuery by notesViewModel.searchQuery.collectAsState()

    // Collect settings
    val currentTheme by settingsViewModel.themeFlow.collectAsState()
    val currentSortOrder by settingsViewModel.sortOrderFlow.collectAsState()

    // Collect favorites reactively
    val favoriteNotes by notesViewModel.getFavoriteNotes().collectAsState(emptyList())

    // Drawer state untuk Navigation Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Track current route untuk bottom nav selection
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom nav items (3 tabs — Settings di drawer saja)
    val bottomNavItems = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    // Determine apakah bottom nav harus ditampilkan
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer Header
                Surface(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "📝 My Notes App",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                HorizontalDivider()

                // Drawer items
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Notes") },
                    selected = currentRoute == Screen.NoteList.route,
                    onClick = {
                        navController.navigate(Screen.NoteList.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Favorites") },
                    selected = currentRoute == Screen.Favorites.route,
                    onClick = {
                        navController.navigate(Screen.Favorites.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = currentRoute == Screen.Profile.route,
                    onClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Add New Note") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.AddNote.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                // Settings — diakses dari drawer (Opsi 3)
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Settings") },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text("About") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(
                        navController = navController,
                        items = bottomNavItems,
                        currentRoute = currentRoute
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.NoteList.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                // --- Tab 1: Note List (dengan search) ---
                composable(Screen.NoteList.route) {
                    NoteListScreen(
                        uiState = notesUiState,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { notesViewModel.onSearchQueryChange(it) },
                        onClearSearch = { notesViewModel.clearSearch() },
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddClick = {
                            navController.navigate(Screen.AddNote.route)
                        },
                        onFavoriteClick = { noteId ->
                            notesViewModel.toggleFavorite(noteId)
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

                // --- Tab 2: Favorites ---
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        favoriteNotes = favoriteNotes,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onFavoriteClick = { noteId ->
                            notesViewModel.toggleFavorite(noteId)
                        },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

                // --- Tab 3: Profile ---
                composable(Screen.Profile.route) {
                    ProfileScreenContent(
                        uiState = profileUiState,
                        onToggleDarkMode = onToggleDarkMode,
                        onToggleEditing = onToggleEditing,
                        onUpdateProfile = onUpdateProfile,
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

                // --- Note Detail (with noteId argument as Long) ---
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.read { getLong("noteId") } ?: 0L
                    var note by remember { mutableStateOf<NoteUi?>(null) }

                    LaunchedEffect(noteId) {
                        note = notesViewModel.getNoteById(noteId)
                    }

                    NoteDetailScreen(
                        note = note,
                        onBack = { navController.popBackStack() },
                        onEdit = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        },
                        onDelete = { id ->
                            notesViewModel.deleteNote(id)
                        },
                        onFavoriteToggle = { id ->
                            notesViewModel.toggleFavorite(id)
                            // Refresh note after toggle
                            scope.launch {
                                note = notesViewModel.getNoteById(id)
                            }
                        }
                    )
                }

                // --- Add Note ---
                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        onBack = { navController.popBackStack() },
                        onSave = { title, content ->
                            notesViewModel.addNote(title, content)
                        }
                    )
                }

                // --- Edit Note (with noteId argument as Long) ---
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.read { getLong("noteId") } ?: 0L
                    var note by remember { mutableStateOf<NoteUi?>(null) }

                    LaunchedEffect(noteId) {
                        note = notesViewModel.getNoteById(noteId)
                    }

                    EditNoteScreen(
                        note = note,
                        onBack = { navController.popBackStack() },
                        onSave = { id, title, content ->
                            notesViewModel.updateNote(id, title, content)
                        }
                    )
                }

                // --- Settings (diakses dari drawer) ---
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        currentTheme = currentTheme,
                        currentSortOrder = currentSortOrder,
                        onThemeChange = { settingsViewModel.setTheme(it) },
                        onSortOrderChange = { settingsViewModel.setSortOrder(it) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

/**
 * Bottom Navigation Bar component.
 * Menampilkan 3 tabs dengan state tracking (current selected).
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>,
    currentRoute: String?
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
