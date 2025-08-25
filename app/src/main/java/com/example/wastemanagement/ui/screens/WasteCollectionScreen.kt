package com.example.wastemanagement.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wastemanagement.ui.localization.LanguageManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteCollectionScreen(navController: NavController, languageManager: LanguageManager) {
    var showScheduleDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedWasteType by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = com.example.wastemanagement.ui.localization.AppLanguage.ENGLISH)
    val wasteTypes = listOf(
        languageManager.getLocalizedString("waste_type_general"),
        languageManager.getLocalizedString("waste_type_recyclables"),
        languageManager.getLocalizedString("waste_type_organic"),
        languageManager.getLocalizedString("waste_type_hazardous"),
        languageManager.getLocalizedString("waste_type_electronics")
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        languageManager.getLocalizedString("waste_collection_title"), 
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { /* TODO: Filter */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showScheduleDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Schedule Pickup")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(600, easing = EaseOutBack)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = languageManager.getLocalizedStringForLanguage(currentLanguage, "collection_overview_title"),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = languageManager.getLocalizedStringForLanguage(currentLanguage, "collection_overview_desc"),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
            
            // Upcoming Collections
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(800, easing = EaseOutBack),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(800))
                ) {
                    Text(
                        text = languageManager.getLocalizedStringForLanguage(currentLanguage, "upcoming_collections"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                    )
                }
            }
            
            // Sample upcoming collections with staggered animation
            itemsIndexed(getSampleCollections()) { index, collection ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(800 + (index * 100), easing = EaseOutBack),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(800 + (index * 100)))
                ) {
                    CollectionCard(collection = collection)
                }
            }
            
            // Recent Collections
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(1000, easing = EaseOutBack),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(1000))
                ) {
                    Text(
                        text = languageManager.getLocalizedStringForLanguage(currentLanguage, "recent_collections"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                    )
                }
            }
            
            itemsIndexed(getSampleRecentCollections()) { index, collection ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(1000 + (index * 100), easing = EaseOutBack),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(1000 + (index * 100)))
                ) {
                    CollectionCard(collection = collection, isCompleted = true)
                }
            }
        }
        
        // Schedule Dialog
        if (showScheduleDialog) {
            ScheduleCollectionDialog(
                onDismiss = { showScheduleDialog = false },
                onSchedule = { date, wasteType ->
                    // TODO: Implement scheduling logic
                    showScheduleDialog = false
                },
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                selectedWasteType = selectedWasteType,
                onWasteTypeSelected = { selectedWasteType = it },
                wasteTypes = wasteTypes,
                languageManager = languageManager,
                currentLanguage = currentLanguage
            )
        }
    }
}

@Composable
fun CollectionCard(
    collection: CollectionItem,
    isCompleted: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (collection.wasteType) {
                    "General Waste" -> Icons.Default.Delete
                    "Recyclables" -> Icons.Default.Star
                    "Organic" -> Icons.Default.Star
                                            "Hazardous" -> Icons.Default.Star
                                            "Electronics" -> Icons.Default.Star
                    else -> Icons.Default.Delete
                },
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = when (collection.wasteType) {
                    "General Waste" -> MaterialTheme.colorScheme.error
                    "Recyclables" -> MaterialTheme.colorScheme.primary
                    "Organic" -> MaterialTheme.colorScheme.secondary
                    "Hazardous" -> MaterialTheme.colorScheme.error
                    "Electronics" -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = collection.wasteType,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = collection.date,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (collection.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = collection.notes,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 16.sp
                    )
                }
            }
            
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                IconButton(onClick = { /* TODO: Edit collection */ }) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleCollectionDialog(
    onDismiss: () -> Unit,
    onSchedule: (LocalDate, String) -> Unit,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    selectedWasteType: String,
    onWasteTypeSelected: (String) -> Unit,
    wasteTypes: List<String>,
    languageManager: com.example.wastemanagement.ui.localization.LanguageManager,
    currentLanguage: com.example.wastemanagement.ui.localization.AppLanguage
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "schedule_collection_title"),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ) 
        },
        text = {
            Column {
                Text(
                    text = languageManager.getLocalizedStringForLanguage(currentLanguage, "select_date"),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Enhanced date picker
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { 
                            onDateSelected(selectedDate.minusDays(1))
                        }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Day")
                        }
                        
                        Text(
                            text = selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        IconButton(onClick = { 
                            onDateSelected(selectedDate.plusDays(1))
                        }) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Day")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = languageManager.getLocalizedStringForLanguage(currentLanguage, "waste_type"),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                wasteTypes.forEach { wasteType ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedWasteType == wasteType) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedWasteType == wasteType,
                                onClick = { onWasteTypeSelected(wasteType) }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                wasteType,
                                fontSize = 16.sp,
                                color = if (selectedWasteType == wasteType) 
                                    MaterialTheme.colorScheme.onPrimaryContainer 
                                else 
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSchedule(selectedDate, selectedWasteType) },
                enabled = selectedWasteType.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(languageManager.getLocalizedStringForLanguage(currentLanguage, "schedule_button"), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(languageManager.getLocalizedStringForLanguage(currentLanguage, "cancel_button"))
            }
        }
    )
}

// Data classes
data class CollectionItem(
    val id: String,
    val wasteType: String,
    val date: String,
    val notes: String = ""
)

// Sample data
fun getSampleCollections(): List<CollectionItem> {
    return listOf(
        CollectionItem("1", "Recyclables", "Tomorrow, 9:00 AM", "Paper, plastic, glass"),
        CollectionItem("2", "Organic", "Friday, 2:00 PM", "Food waste and garden trimmings"),
        CollectionItem("3", "Electronics", "Next Monday, 11:00 AM", "Old phones and laptops")
    )
}

fun getSampleRecentCollections(): List<CollectionItem> {
    return listOf(
        CollectionItem("4", "General Waste", "Yesterday, 10:00 AM"),
        CollectionItem("5", "Hazardous", "Last Week, 3:00 PM", "Paint and chemicals")
    )
}
