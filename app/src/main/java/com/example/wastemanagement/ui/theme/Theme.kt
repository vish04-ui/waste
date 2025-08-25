package com.example.wastemanagement.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// EcoGrid brand colors based on the logo
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66CC00), // Vibrant lime green from logo
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4F5B47), // Dark olive green from logo
    onPrimaryContainer = Color(0xFFA5D6A7),
    
    secondary = Color(0xFF66CC00), // Vibrant lime green
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF333333), // Dark gray from logo
    onSecondaryContainer = Color(0xFFC8E6C9),
    
    tertiary = Color(0xFF4F5B47), // Dark olive green
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF2E7D32),
    onTertiaryContainer = Color(0xFFBBDEFB),
    
    error = Color(0xFFEF5350), // Red
    onError = Color.White,
    errorContainer = Color(0xFFC62828),
    onErrorContainer = Color(0xFFEF9A9A),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFBDBDBD)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF66CC00), // Vibrant lime green from logo
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E8),
    onPrimaryContainer = Color(0xFF4F5B47), // Dark olive green from logo
    
    secondary = Color(0xFF4F5B47), // Dark olive green
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0F4F0),
    onSecondaryContainer = Color(0xFF4F5B47),
    
    tertiary = Color(0xFF333333), // Dark gray from logo
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF5F5F5),
    onTertiaryContainer = Color(0xFF333333),
    
    error = Color(0xFFD32F2F), // Red
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF212121),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242)
)

@Composable
fun EcoGridTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun EcoGridTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
    
    EcoGridTheme(darkTheme = isDarkMode, content = content)
}