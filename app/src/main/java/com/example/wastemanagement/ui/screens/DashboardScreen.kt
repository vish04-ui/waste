package com.example.wastemanagement.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wastemanagement.R
import com.example.wastemanagement.ui.theme.ThemeManager
import com.example.wastemanagement.ui.localization.LanguageManager
import com.example.wastemanagement.ui.localization.LanguageSelector
import com.example.wastemanagement.ui.localization.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, themeManager: ThemeManager, languageManager: LanguageManager) {
    var showLanguageSelector by remember { mutableStateOf(false) }
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.ecogrid_wordmark),
                        contentDescription = null,
                        modifier = Modifier.height(100.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Language toggle button
                        OutlinedCard(
                            onClick = { showLanguageSelector = true },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = currentLanguage.code.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        
                        // Theme toggle button
                        val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
                        IconButton(
                            onClick = {
                                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                                    themeManager.toggleTheme()
                                }
                            },
                            modifier = Modifier.sizeIn(minWidth = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget, minHeight = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget)
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    // Language selector dialog
                    if (showLanguageSelector) {
                        LanguageSelector(
                            languageManager = languageManager,
                            onDismiss = { showLanguageSelector = false }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Eco,
                            contentDescription = "App Icon",
                            modifier = Modifier.size(56.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = languageManager.getLocalizedStringForLanguage(currentLanguage, "welcome_title"),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = languageManager.getLocalizedStringForLanguage(currentLanguage, "welcome_subtitle"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Quick Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    icon = Icons.Default.CheckCircle,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "collections"),
                    value = "12",
                    modifier = Modifier
                        .weight(1f)
                )
                StatCard(
                    icon = Icons.Default.Autorenew,
                    title = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycled"),
                    value = "85%",
                    modifier = Modifier
                        .weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Feature Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                item {
                    FeatureCard(
                        icon = Icons.Default.CalendarMonth,
                        title = languageManager.getLocalizedStringForLanguage(currentLanguage, "waste_collection"),
                        description = languageManager.getLocalizedStringForLanguage(currentLanguage, "waste_collection_desc"),
                        onClick = { navController.navigate("waste_collection") }
                    )
                }
                item {
                    FeatureCard(
                        icon = Icons.Default.Autorenew,
                        title = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycling_guide"),
                        description = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycling_guide_desc"),
                        onClick = { navController.navigate("recycling_guide") }
                    )
                }
                item {
                    FeatureCard(
                        icon = Icons.Default.Person,
                        title = languageManager.getLocalizedStringForLanguage(currentLanguage, "profile"),
                        description = languageManager.getLocalizedStringForLanguage(currentLanguage, "profile_desc"),
                        onClick = { navController.navigate("profile") }
                    )
                }
                item {
                    FeatureCard(
                        icon = Icons.Default.History,
                        title = languageManager.getLocalizedStringForLanguage(currentLanguage, "history"),
                        description = languageManager.getLocalizedStringForLanguage(currentLanguage, "history_desc"),
                        onClick = { /* TODO: Implement history */ }
                    )
                }
            }
        }
    }
}

// Simplified version for fragment host (no Nav navigation inside grid; switching handled by Activity)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenStub(themeManager: ThemeManager, languageManager: LanguageManager) {
    var showLanguageSelector by remember { mutableStateOf(false) }
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = AppLanguage.ENGLISH)
    Scaffold(topBar = {
        TopAppBar(title = {
            Image(painter = painterResource(id = R.drawable.ecogrid_wordmark), contentDescription = null, modifier = Modifier.height(72.dp), contentScale = androidx.compose.ui.layout.ContentScale.Fit)
        }, actions = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedCard(onClick = { showLanguageSelector = true }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)), modifier = Modifier.padding(4.dp)) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Text(text = currentLanguage.code.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
                val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
                IconButton(onClick = { kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch { themeManager.toggleTheme() } }) {
                    Icon(imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            if (showLanguageSelector) {
                LanguageSelector(languageManager = languageManager, onDismiss = { showLanguageSelector = false })
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer))
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            ElevatedCard(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Rounded.Eco, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = languageManager.getLocalizedStringForLanguage(currentLanguage, "welcome_title"), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = languageManager.getLocalizedStringForLanguage(currentLanguage, "welcome_subtitle"), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f), textAlign = TextAlign.Center)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(icon = Icons.Default.CheckCircle, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "collections"), value = "12", modifier = Modifier.weight(1f))
                StatCard(icon = Icons.Default.Autorenew, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycled"), value = "85%", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth().height(400.dp)) {
                item { FeatureCard(icon = Icons.Default.CalendarMonth, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "waste_collection"), description = languageManager.getLocalizedStringForLanguage(currentLanguage, "waste_collection_desc"), onClick = { /* handled by bottom nav */ }) }
                item { FeatureCard(icon = Icons.Default.Autorenew, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycling_guide"), description = languageManager.getLocalizedStringForLanguage(currentLanguage, "recycling_guide_desc"), onClick = { }) }
                item { FeatureCard(icon = Icons.Default.Person, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "profile"), description = languageManager.getLocalizedStringForLanguage(currentLanguage, "profile_desc"), onClick = { }) }
                item { FeatureCard(icon = Icons.Default.History, title = languageManager.getLocalizedStringForLanguage(currentLanguage, "history"), description = languageManager.getLocalizedStringForLanguage(currentLanguage, "history_desc"), onClick = { }) }
            }
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .heightIn(min = 112.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .sizeIn(minHeight = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
