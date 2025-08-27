package com.example.wastemanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.wastemanagement.ui.auth.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeManager = ThemeManager(this)
            val languageManager = LanguageManager(this)
            EcoGridTheme(themeManager = themeManager) {
                val authManager = AuthManager(this)
                WasteManagementApp(themeManager, languageManager, authManager)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteManagementApp(themeManager: ThemeManager, languageManager: LanguageManager, authManager: AuthManager) {
    val navController = rememberNavController()
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    val hasOnboarded by authManager.hasCompletedOnboarding.collectAsState(initial = false)
    val isLoggedIn by authManager.isLoggedIn.collectAsState(initial = false)
    val startDestination = "router"
    
    LaunchedEffect(currentLanguage) {
        languageManager.applyLanguage(currentLanguage)
    }
    
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val bottomRoutes = setOf("dashboard", "waste_collection", "recycling_guide", "profile")
            if (currentRoute in bottomRoutes) {
                NavigationBar {
                    val items = listOf(
                        NavigationItem(
                            route = "dashboard",
                            title = stringResource(id = R.string.dashboard),
                            icon = Icons.Default.Home
                        ),
                        NavigationItem(
                            route = "waste_collection",
                            title = stringResource(id = R.string.collection),
                            icon = Icons.Default.Delete
                        ),
                        NavigationItem(
                            route = "recycling_guide",
                            title = stringResource(id = R.string.recycling),
                            icon = Icons.Default.Info
                        ),
                        NavigationItem(
                            route = "profile",
                            title = stringResource(id = R.string.profile),
                            icon = Icons.Default.Person
                        )
                    )
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
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
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
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
            composable("router") {
                // Decide initial route based on persisted flags
                val destination = when {
                    !hasOnboarded -> "onboarding"
                    !isLoggedIn -> "login"
                    else -> "dashboard"
                }
                LaunchedEffect(destination) {
                    navController.navigate(destination) {
                        popUpTo("router") { inclusive = true }
                    }
                }
            }
            composable("onboarding") {
                OnboardingScreen(navController, languageManager, authManager)
            }
            composable("login") {
                LoginScreen(navController, languageManager, authManager)
            }
            composable("signup") {
                SignupScreen(navController, languageManager, authManager)
            }
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
                ProfileScreen(navController, themeManager, languageManager, authManager)
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)