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
import com.bintang.myprofileapp.viewmodel.AIViewModel
import com.bintang.myprofileapp.viewmodel.NotesViewModel
import com.bintang.myprofileapp.viewmodel.ProfileUiState
import com.bintang.myprofileapp.viewmodel.ProfileViewModel
import com.bintang.myprofileapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val notesViewModel: NotesViewModel = koinViewModel()
    val settingsViewModel: SettingsViewModel = koinInject()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val aiViewModel: AIViewModel = koinViewModel()

    val notesUiState by notesViewModel.uiState.collectAsState()
    val searchQuery by notesViewModel.searchQuery.collectAsState()

    val profileUiState by profileViewModel.uiState.collectAsState()

    val currentTheme by settingsViewModel.themeFlow.collectAsState()
    val currentSortOrder by settingsViewModel.sortOrderFlow.collectAsState()

    val favoriteNotes by notesViewModel.getFavoriteNotes().collectAsState(emptyList())

    // AI states
    val chatState by aiViewModel.chatState.collectAsState()
    val summaryState by aiViewModel.summaryState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.AIChat,
        BottomNavItem.Profile
    )

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
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
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                    label = { Text("AI Chat") },
                    selected = currentRoute == Screen.AIChat.route,
                    onClick = {
                        navController.navigate(Screen.AIChat.route) {
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

                // AI Chat Screen
                composable(Screen.AIChat.route) {
                    AIChatScreen(
                        chatState = chatState,
                        onSendMessage = { message -> aiViewModel.sendMessage(message) },
                        onRequestRecommendations = { aiViewModel.requestRecommendations() },
                        onClearChat = { aiViewModel.clearChat() },
                        onClearError = { aiViewModel.clearError() },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreenContent(
                        uiState = profileUiState,
                        onToggleDarkMode = { settingsViewModel.toggleDarkMode() },
                        onToggleEditing = { profileViewModel.toggleEditing() },
                        onUpdateProfile = { name, bio -> profileViewModel.updateProfile(name, bio) },
                        onMenuClick = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }

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
                            scope.launch {
                                note = notesViewModel.getNoteById(id)
                            }
                        },
                        onSummarize = { id ->
                            aiViewModel.summarizeNote(id)
                            navController.navigate(Screen.NoteSummary.createRoute(id))
                        }
                    )
                }

                // AI Note Summary Screen
                composable(
                    route = Screen.NoteSummary.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) {
                    NoteSummaryScreen(
                        summaryState = summaryState,
                        onBack = {
                            aiViewModel.clearSummary()
                            navController.popBackStack()
                        },
                        onRetry = { aiViewModel.retrySummary() }
                    )
                }

                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        onBack = { navController.popBackStack() },
                        onSave = { title, content ->
                            notesViewModel.addNote(title, content)
                        }
                    )
                }

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
