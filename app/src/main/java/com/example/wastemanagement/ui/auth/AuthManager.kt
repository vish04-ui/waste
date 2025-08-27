package com.example.wastemanagement.ui.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

class AuthManager(private val context: Context) {
    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val HAS_ONBOARDED = booleanPreferencesKey("has_onboarded")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
    }

    val isLoggedIn: Flow<Boolean> = context.authDataStore.data
        .map { it[IS_LOGGED_IN] ?: false }

    val hasCompletedOnboarding: Flow<Boolean> = context.authDataStore.data
        .map { it[HAS_ONBOARDED] ?: false }

    val userName: Flow<String> = context.authDataStore.data
        .map { it[USER_NAME] ?: "" }

    val userEmail: Flow<String> = context.authDataStore.data
        .map { it[USER_EMAIL] ?: "" }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.authDataStore.edit { prefs ->
            prefs[HAS_ONBOARDED] = completed
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        // In a real app, authenticate with backend here
        context.authDataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[USER_EMAIL] = email
        }
        return true
    }

    suspend fun signup(name: String, email: String, password: String): Boolean {
        // In a real app, call backend signup
        context.authDataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
        return true
    }

    suspend fun logout() {
        context.authDataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }
}



