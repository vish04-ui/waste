package com.example.wastemanagement.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wastemanagement.ui.auth.AuthManager
import com.example.wastemanagement.ui.localization.LanguageManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    languageManager: LanguageManager,
    authManager: AuthManager
) {
    val pages = listOf(
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_1),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_1)
        ),
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_2),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_2)
        ),
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_3),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_3)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    scope.launch {
                        authManager.setOnboardingCompleted(true)
                        navController.navigate("login") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                }) {
                    Text(stringResource(id = com.example.wastemanagement.R.string.skip))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${pages.size}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        scope.launch {
                            if (pagerState.currentPage < pages.lastIndex) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                authManager.setOnboardingCompleted(true)
                                navController.navigate("login") {
                                    popUpTo("onboarding") { inclusive = true }
                                }
                            }
                        }
                    }, modifier = Modifier.heightIn(min = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget)) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (pagerState.currentPage < pages.lastIndex)
                                stringResource(id = com.example.wastemanagement.R.string.next)
                            else stringResource(id = com.example.wastemanagement.R.string.finish),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPage(
                    title = pages[page].first,
                    description = pages[page].second,
                )
            }
        }
    }
}

// Wrapper for activity-based flow (no NavController). Reuses core UI logic via callback.
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreenStandalone(
    languageManager: LanguageManager,
    authManager: AuthManager,
    onFinished: () -> Unit
) {
    val pages = listOf(
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_1),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_1)
        ),
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_2),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_2)
        ),
        Pair(
            stringResource(id = com.example.wastemanagement.R.string.onboarding_title_3),
            stringResource(id = com.example.wastemanagement.R.string.onboarding_desc_3)
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    Scaffold(contentWindowInsets = WindowInsets.safeDrawing, bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = {
                scope.launch {
                    authManager.setOnboardingCompleted(true)
                    onFinished()
                }
            }) {
                Text(stringResource(id = com.example.wastemanagement.R.string.skip))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${pagerState.currentPage + 1}/${pages.size}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = {
                    scope.launch {
                        if (pagerState.currentPage < pages.lastIndex) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            authManager.setOnboardingCompleted(true)
                            onFinished()
                        }
                    }
                }, modifier = Modifier.heightIn(min = com.example.wastemanagement.ui.theme.Dimens.minTouchTarget)) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pagerState.currentPage < pages.lastIndex)
                            stringResource(id = com.example.wastemanagement.R.string.next)
                        else stringResource(id = com.example.wastemanagement.R.string.finish),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPage(
                    title = pages[page].first,
                    description = pages[page].second,
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    title: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(88.dp)
                    .padding(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}



