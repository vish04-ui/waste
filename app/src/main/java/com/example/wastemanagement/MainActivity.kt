package com.example.wastemanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.wastemanagement.ui.localization.AppLanguage
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wastemanagement.ui.screens.*
import com.example.wastemanagement.ui.theme.EcoGridTheme
import com.example.wastemanagement.ui.theme.ThemeManager
import com.example.wastemanagement.ui.localization.LanguageManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeManager = ThemeManager(this)
            val languageManager = LanguageManager(this)
            EcoGridTheme(themeManager = themeManager) {
                WasteManagementApp(themeManager, languageManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteManagementApp(themeManager: ThemeManager, languageManager: LanguageManager) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    NavigationItem(
                        route = "dashboard",
                        title = languageManager.getLocalizedString("dashboard"),
                        icon = Icons.Default.Home
                    ),
                    NavigationItem(
                        route = "waste_collection",
                        title = languageManager.getLocalizedString("collection"),
                        icon = Icons.Default.Delete
                    ),
                    NavigationItem(
                        route = "recycling_guide",
                        title = languageManager.getLocalizedString("recycling"),
                        icon = Icons.Default.Info
                    ),
                    NavigationItem(
                        route = "profile",
                        title = languageManager.getLocalizedString("profile"),
                        icon = Icons.Default.Person
                    )
                )
                
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            if (currentDestination?.route != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            composable("dashboard") {
                DashboardScreen(navController, themeManager, languageManager)
            }
            composable("waste_collection") {
                WasteCollectionScreen(navController, languageManager)
            }
            composable("recycling_guide") {
                RecyclingGuideScreen(navController, languageManager)
            }
            composable("profile") {
                ProfileScreen(navController, themeManager, languageManager)
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)