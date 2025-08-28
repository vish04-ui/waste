package com.example.wastemanagement.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wastemanagement.ui.auth.AuthManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Splash / router activity that decides the first real screen.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // No UI (uses splash theme). Route after quick auth checks.
        val authManager = AuthManager(this)
        lifecycleScope.launch {
            val hasOnboarded = authManager.hasCompletedOnboarding.first()
            val isLoggedIn = authManager.isLoggedIn.first()
            when {
                !hasOnboarded -> startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                !isLoggedIn -> startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                else -> startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}
