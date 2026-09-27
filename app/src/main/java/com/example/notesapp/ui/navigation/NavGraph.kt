package com.example.notesapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.data.NoteDao
import com.example.notesapp.ui.screens.FirstScreen
import com.example.notesapp.ui.screens.SecondScreen

/** Route string constants used throughout the nav graph. */
object Routes {
    const val ADD_NOTE = "add_note"
    const val NOTES_LIST = "notes_list"
}

/** Describes a single bottom navigation destination. */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object AddNote : BottomNavItem(Routes.ADD_NOTE, "Add Note", Icons.Filled.Add)
    object NotesList : BottomNavItem(Routes.NOTES_LIST, "Notes", Icons.Filled.List)
}

private val bottomNavItems = listOf(BottomNavItem.AddNote, BottomNavItem.NotesList)

/** Material 3 bottom navigation bar wired to [navController]. */
@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid building up a large stack as the user switches tabs.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

/**
 * Hosts the navigation graph. [contentPadding] should come from the single
 * top-level Scaffold in [MainScreen] — do not add another Scaffold here.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    noteDao: NoteDao,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.ADD_NOTE,
        modifier = modifier.padding(contentPadding)
    ) {
        composable(Routes.ADD_NOTE) {
            FirstScreen(noteDao = noteDao, snackbarHostState = snackbarHostState)
        }
        composable(Routes.NOTES_LIST) {
            SecondScreen(noteDao = noteDao)
        }
    }
}

/**
 * Single top-level Scaffold for the whole app. Screens must NOT introduce
 * their own Scaffold/bottomBar to avoid double padding or nested insets.
 */
@Composable
fun MainScreen(noteDao: NoteDao) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { AppBottomNavigation(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            noteDao = noteDao,
            snackbarHostState = snackbarHostState,
            contentPadding = innerPadding
        )
    }
}
