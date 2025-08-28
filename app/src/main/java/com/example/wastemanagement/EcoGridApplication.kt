package com.example.wastemanagement

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.wastemanagement.ui.theme.ThemeManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EcoGridApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize theme on app startup
        val themeManager = ThemeManager(this)
        
        // Apply saved theme preference synchronously
        runBlocking {
            val isDark = themeManager.isDarkMode.first()
            val nightMode = if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}
