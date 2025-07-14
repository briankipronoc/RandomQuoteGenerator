package com.kiprono.randomquote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey // ADDED: Import for boolean key
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore keys
private val USER_NAME_KEY = stringPreferencesKey("user_name")
private val QUOTES_READ_COUNT_KEY = intPreferencesKey("quotes_read_count")
private val QUOTES_LIKED_COUNT_KEY = intPreferencesKey("quotes_liked_count")
private val QUOTES_SHARED_COUNT_KEY = intPreferencesKey("quotes_shared_count")
private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key") // ADDED: Key for dark theme

// Provide a DataStore instance at the application level
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(
    private val context: Context
) {
    private val dataStore = context.dataStore

    // Expose userName as a Flow
    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    // Expose quotesReadCount as a Flow
    val quotesReadCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUOTES_READ_COUNT_KEY] ?: 0
    }

    // Expose quotesLikedCount as a Flow
    val quotesLikedCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUOTES_LIKED_COUNT_KEY] ?: 0
    }

    // Expose quotesSharedCount as a Flow
    val quotesSharedCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUOTES_SHARED_COUNT_KEY] ?: 0
    }

    // ADDED: Expose isDarkTheme as a Flow
    val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false // Default to false (light theme) if not set
    }

    // Function to save the user name
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // Function to save quotes read count
    suspend fun saveQuotesReadCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_READ_COUNT_KEY] = count
        }
    }

    // Function to save quotes liked count
    suspend fun saveQuotesLikedCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_LIKED_COUNT_KEY] = count
        }
    }

    // Function to save quotes shared count
    suspend fun saveQuotesSharedCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_SHARED_COUNT_KEY] = count
        }
    }

    // ADDED: Function to save the dark theme preference
    suspend fun saveThemePreference(isDark: Boolean) { // <--- ADDED THIS FUNCTION
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }

    // The UserStats data class and readStats/saveStats functions
    // (kept as they were in your provided code, assuming they are still used elsewhere)
    data class UserStats(
        val readCount: Int,
        val sharedCount: Int,
        val likedCount: Int
    )

    val readStats: Flow<UserStats> = dataStore.data.map { preferences ->
        UserStats(
            readCount = preferences[QUOTES_READ_COUNT_KEY] ?: 0,
            sharedCount = preferences[QUOTES_SHARED_COUNT_KEY] ?: 0,
            likedCount = preferences[QUOTES_LIKED_COUNT_KEY] ?: 0
        )
    }

    suspend fun saveStats(readCount: Int, sharedCount: Int, likedCount: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_READ_COUNT_KEY] = readCount
            preferences[QUOTES_SHARED_COUNT_KEY] = sharedCount
            preferences[QUOTES_LIKED_COUNT_KEY] = likedCount
        }
    }
}