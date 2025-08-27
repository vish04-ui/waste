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
import androidx.compose.ui.res.stringResource
import com.example.wastemanagement.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecyclingGuideScreen(navController: NavController, languageManager: LanguageManager) {
    var expandedCategory by remember { mutableStateOf<String?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    val currentLanguage by languageManager.currentLanguage.collectAsState(initial = com.example.wastemanagement.ui.localization.AppLanguage.ENGLISH)
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(id = R.string.recycling_guide_title), 
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
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
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Info, contentDescription = "Search")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with animation
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(600, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Recycling Icon",
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(id = R.string.recycling_header_title),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.recycling_header_subtitle),
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Recycling Categories with staggered animation
            itemsIndexed(getRecyclingCategories(languageManager, currentLanguage)) { index, category ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(800 + (index * 150), easing = FastOutSlowInEasing),
                        initialOffsetX = { -it }
                    ) + fadeIn(animationSpec = tween(800 + (index * 150)))
                ) {
                    RecyclingCategoryCard(
                        category = category,
                        isExpanded = expandedCategory == category.id,
                        onToggle = { 
                            expandedCategory = if (expandedCategory == category.id) null else category.id
                        },
                        languageManager = languageManager,
                        currentLanguage = currentLanguage
                    )
                }
            }
            
            // Quick Tips section
            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        animationSpec = tween(1200, easing = FastOutSlowInEasing),
                        initialOffsetY = { it }
                    ) + fadeIn(animationSpec = tween(1200))
                ) {
                    Text(
                        text = stringResource(id = R.string.quick_tips),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                }
            }
            
            // Quick Tips with staggered animation
            itemsIndexed(getQuickTips(languageManager, currentLanguage)) { index, tip ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInHorizontally(
                        animationSpec = tween(1400 + (index * 100), easing = FastOutSlowInEasing),
                        initialOffsetX = { it }
                    ) + fadeIn(animationSpec = tween(1400 + (index * 100)))
                ) {
                    QuickTipCard(tip = tip)
                }
            }
        }
    }
}

@Composable
fun RecyclingCategoryCard(
    category: RecyclingCategory,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    languageManager: com.example.wastemanagement.ui.localization.LanguageManager,
    currentLanguage: com.example.wastemanagement.ui.localization.AppLanguage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = when (category.id) {
                        "paper" -> MaterialTheme.colorScheme.primary
                        "plastic" -> MaterialTheme.colorScheme.secondary
                        "glass" -> MaterialTheme.colorScheme.tertiary
                        "metal" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = category.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(
                    animationSpec = tween(300, easing = EaseInBack)
                ) + fadeOut(animationSpec = tween(300))
            ) {
                Divider()
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = languageManager.getLocalizedStringForLanguage(currentLanguage, "what_to_include"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    category.items.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    if (category.tips.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = languageManager.getLocalizedStringForLanguage(currentLanguage, "tips_label"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        category.tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = tip,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickTipCard(
    tip: QuickTip,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = tip.icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = tip.text,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

// Data classes
data class RecyclingCategory(
    val id: String,
    val name: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val items: List<String>,
    val tips: List<String>
)

data class QuickTip(
    val text: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

// Sample data with better icons
fun getRecyclingCategories(languageManager: com.example.wastemanagement.ui.localization.LanguageManager, currentLanguage: com.example.wastemanagement.ui.localization.AppLanguage): List<RecyclingCategory> {
    return listOf(
        RecyclingCategory(
            id = "paper",
            name = languageManager.getLocalizedStringForLanguage(currentLanguage, "paper_cardboard"),
            description = languageManager.getLocalizedStringForLanguage(currentLanguage, "recyclable_paper_products"),
            icon = Icons.Default.Description,
            items = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_newspapers_magazines"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_cardboard_boxes"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_office_paper"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_junk_mail"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_paper_bags")
            ),
            tips = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_remove_plastic_windows"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_flatten_cardboard"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_keep_paper_dry")
            )
        ),
        RecyclingCategory(
            id = "plastic",
            name = languageManager.getLocalizedStringForLanguage(currentLanguage, "plastic"),
            description = languageManager.getLocalizedStringForLanguage(currentLanguage, "plastic_desc"),
            icon = Icons.Default.Science,
            items = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_water_bottles"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_milk_jugs"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_food_containers"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_shampoo_bottles"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_cleaning_product_bottles")
            ),
            tips = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_rinse_containers"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_check_numbers"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_remove_caps_labels")
            )
        ),
        RecyclingCategory(
            id = "glass",
            name = languageManager.getLocalizedStringForLanguage(currentLanguage, "glass"),
            description = languageManager.getLocalizedStringForLanguage(currentLanguage, "glass_desc"),
            icon = Icons.Default.WineBar,
            items = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_beverage_bottles"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_food_jars"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_condiment_bottles"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_cosmetic_containers")
            ),
            tips = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_rinse_thoroughly"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_remove_metal_lids"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_dont_break_glass")
            )
        ),
        RecyclingCategory(
            id = "metal",
            name = languageManager.getLocalizedStringForLanguage(currentLanguage, "metal"),
            description = languageManager.getLocalizedStringForLanguage(currentLanguage, "metal_desc"),
            icon = Icons.Default.Build,
            items = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_aluminum_cans"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_steel_food_cans"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_aerosol_cans"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "item_aluminum_foil")
            ),
            tips = listOf(
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_rinse_food_residue"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_crush_cans"),
                languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_check_aerosol_empty")
            )
        )
    )
}

fun getQuickTips(languageManager: com.example.wastemanagement.ui.localization.LanguageManager, currentLanguage: com.example.wastemanagement.ui.localization.AppLanguage): List<QuickTip> {
    return listOf(
        QuickTip(languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_rinse_containers"), Icons.Default.Info),
        QuickTip(languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_check_guidelines"), Icons.Default.Info),
        QuickTip(languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_when_in_doubt"), Icons.Default.Delete),
        QuickTip(languageManager.getLocalizedStringForLanguage(currentLanguage, "tip_recycle_batteries"), Icons.Default.Info)
    )
}
