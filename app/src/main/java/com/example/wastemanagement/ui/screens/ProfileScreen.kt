package com.example.wastemanagement.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wastemanagement.ui.theme.ThemeManager
import com.example.wastemanagement.ui.localization.LanguageManager
import com.example.wastemanagement.ui.auth.AuthManager
import com.example.wastemanagement.ui.localization.LanguageSelector
import com.example.wastemanagement.ui.localization.AppLanguage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, themeManager: ThemeManager, languageManager: LanguageManager, authManager: AuthManager) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoScheduleEnabled by remember { mutableStateOf(true) }
    var isVisible by remember { mutableStateOf(false) }
    
    // Collect theme state from ThemeManager
    val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Title Section
            Text(
                text = languageManager.getLocalizedString("profile"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Profile Header with animation
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(600, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(600))
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile Picture Placeholder with gradient
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(56.dp)
                                    .padding(22.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Text(
                            text = "John Doe",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Text(
                            text = "john.doe@email.com",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "collections"), "24", MaterialTheme.colorScheme.primary)
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "recycled"), "156 kg", MaterialTheme.colorScheme.secondary)
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "streak"), "15 days", MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Settings Section with animation
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(800, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(800))
            ) {
                Text(
                    text = languageManager.getLocalizedStringForLanguage(currentLanguage, "settings"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Settings cards with staggered animation
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(900, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(900))
            ) {
                SettingsCard(
                    icon = Icons.Default.Notifications,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "notifications"),
                    subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "notifications_desc"),
                    content = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(1000, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(1000))
            ) {
                SettingsCard(
                    icon = Icons.Default.Settings,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "dark_mode"),
                    subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "dark_mode_desc"),
                    content = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { 
                                // Launch a coroutine to update the theme
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                    themeManager.setDarkMode(it)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(1100, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(1100))
            ) {
                SettingsCard(
                    icon = Icons.Default.Settings,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "auto_schedule"),
                    subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "auto_schedule_desc"),
                    content = {
                        Switch(
                            checked = autoScheduleEnabled,
                            onCheckedChange = { autoScheduleEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(1200, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(1200))
            ) {
                var showLanguageSelector by remember { mutableStateOf(false) }
                val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
                
                SettingsCard(
                    icon = Icons.Default.Info,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "language"),
                    subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "language_desc"),
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = currentLanguage.displayName,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            IconButton(
                                onClick = { showLanguageSelector = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = languageManager.getLocalizedStringForLanguage(currentLanguage, "language"),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                )
                
                if (showLanguageSelector) {
                    LanguageSelector(
                        languageManager = languageManager,
                        onDismiss = { showLanguageSelector = false }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Account Actions section
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInHorizontally(
                    animationSpec = tween(1200, easing = FastOutSlowInEasing),
                    initialOffsetX = { -it }
                ) + fadeIn(animationSpec = tween(1200))
            ) {
                Text(
                    text = languageManager.getLocalizedStringForLanguage(currentLanguage, "account"),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action cards with staggered animation
            val actionCards = listOf(
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "edit_profile"), languageManager.getLocalizedStringForLanguage(currentLanguage, "edit_profile_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "address_settings"), languageManager.getLocalizedStringForLanguage(currentLanguage, "address_settings_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "help_support"), languageManager.getLocalizedStringForLanguage(currentLanguage, "help_support_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "about"), languageManager.getLocalizedStringForLanguage(currentLanguage, "about_desc"))
            )
            
            actionCards.forEachIndexed { index, (icon, title, subtitle) ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(1300 + (index * 100), easing = FastOutSlowInEasing),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(1300 + (index * 100)))
                ) {
                    ActionCard(
                        icon = icon,
                        title = title,
                        subtitle = subtitle,
                        onClick = { /* TODO: Implement action */ }
                    )
                    if (index < actionCards.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Logout Button with animation
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(1500, easing = FastOutSlowInEasing),
                    initialOffsetY = { it }
                ) + fadeIn(animationSpec = tween(1500))
            ) {
                Button(
                    onClick = { 
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                            authManager.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(languageManager.getLocalizedStringForLanguage(currentLanguage, "logout"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenStandalone(languageManager: LanguageManager, themeManager: ThemeManager, authManager: AuthManager) {
    // Reuse original without nav back / logout navigation (activity handles finish)
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoScheduleEnabled by remember { mutableStateOf(true) }
    var isVisible by remember { mutableStateOf(false) }
    val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    LaunchedEffect(Unit) { isVisible = true }
    Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Title Section
            Text(
                text = languageManager.getLocalizedString("profile"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            AnimatedVisibility(visible = isVisible, enter = slideInVertically(animationSpec = tween(600, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(600))) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(modifier = Modifier.size(100.dp), shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.primary) { Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(56.dp).padding(22.dp), tint = MaterialTheme.colorScheme.onPrimary) }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "John Doe", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(text = "john.doe@email.com", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "collections"), "24", MaterialTheme.colorScheme.primary)
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "recycled"), "156 kg", MaterialTheme.colorScheme.secondary)
                            StatItem(languageManager.getLocalizedStringForLanguage(currentLanguage, "streak"), "15 days", MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(800, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(800))) {
                Text(text = languageManager.getLocalizedStringForLanguage(currentLanguage, "settings"), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(900, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(900))) {
                SettingsCard(icon = Icons.Default.Notifications, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "notifications"), subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "notifications_desc"), content = { Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it }) })
            }
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(1000))) {
                SettingsCard(icon = Icons.Default.Settings, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "dark_mode"), subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "dark_mode_desc"), content = { Switch(checked = isDarkMode, onCheckedChange = { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch { themeManager.setDarkMode(it) } }) })
            }
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(1100, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(1100))) {
                SettingsCard(icon = Icons.Default.Settings, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "auto_schedule"), subtitle = languageManager.getLocalizedStringForLanguage(currentLanguage, "auto_schedule_desc"), content = { Switch(checked = autoScheduleEnabled, onCheckedChange = { autoScheduleEnabled = it }) })
            }
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(1200, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(1200))) {
                var showLanguageSelector by remember { mutableStateOf(false) }
                val lang by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
                SettingsCard(icon = Icons.Default.Info, title = languageManager.getLocalizedStringForLanguage(lang, "language"), subtitle = languageManager.getLocalizedStringForLanguage(lang, "language_desc"), content = { Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) { Text(text = lang.displayName, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); IconButton(onClick = { showLanguageSelector = true }) { Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) } } })
                if (showLanguageSelector) LanguageSelector(languageManager = languageManager, onDismiss = { showLanguageSelector = false })
            }
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(1300, easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(1300))) { Text(text = languageManager.getLocalizedStringForLanguage(currentLanguage, "account"), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) }
            Spacer(modifier = Modifier.height(16.dp))
            val actionCards = listOf(
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "edit_profile"), languageManager.getLocalizedStringForLanguage(currentLanguage, "edit_profile_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "address_settings"), languageManager.getLocalizedStringForLanguage(currentLanguage, "address_settings_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "help_support"), languageManager.getLocalizedStringForLanguage(currentLanguage, "help_support_desc")),
                Triple(Icons.Default.Info, languageManager.getLocalizedStringForLanguage(currentLanguage, "about"), languageManager.getLocalizedStringForLanguage(currentLanguage, "about_desc"))
            )
            actionCards.forEachIndexed { index, (icon, title, subtitle) ->
                AnimatedVisibility(visible = isVisible, enter = slideInHorizontally(animationSpec = tween(1400 + (index * 100), easing = FastOutSlowInEasing), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(1400 + (index * 100)))) {
                    ActionCard(icon = icon, title = title, subtitle = subtitle, onClick = { })
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(visible = isVisible, enter = slideInVertically(animationSpec = tween(1500, easing = FastOutSlowInEasing), initialOffsetY = { it }) + fadeIn(animationSpec = tween(1500))) {
                Button(onClick = { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch { authManager.logout() } }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(languageManager.getLocalizedStringForLanguage(currentLanguage, "logout"), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(18.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
            
            content()
        }
    }
}

@Composable
fun ActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(18.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 18.sp
                )
            }
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
