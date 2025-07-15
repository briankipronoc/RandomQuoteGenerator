package com.kiprono.randomquote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Create a DataStore instance. This should be a top-level property
// or declared in a DI module. For simplicity here, we'll declare it here.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val USER_NAME = stringPreferencesKey("user_name")
        val QUOTES_READ_COUNT = intPreferencesKey("quotes_read_count")
        val FAVORITE_QUOTES_COUNT = intPreferencesKey("favorite_quotes_count")
        val QUOTES_SHARED_COUNT = intPreferencesKey("quotes_shared_count")
    }

    // Theme preference
    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] ?: false // Default to false (light theme)
        }

    suspend fun saveThemePreference(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }

    // User Name
    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_NAME] ?: "Guest" // Default name
        }

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    // Quotes Read Count
    val quotesReadCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.QUOTES_READ_COUNT] ?: 0 // Default to 0
        }

    suspend fun incrementQuotesRead() {
        context.dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.QUOTES_READ_COUNT] ?: 0
            preferences[PreferencesKeys.QUOTES_READ_COUNT] = currentCount + 1
        }
    }

    // Favorite Quotes Count
    val favoriteQuotesCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FAVORITE_QUOTES_COUNT] ?: 0 // Default to 0
        }

    suspend fun updateFavoriteQuotesCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FAVORITE_QUOTES_COUNT] = count
        }
    }

    // Quotes Shared Count
    val quotesSharedCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.QUOTES_SHARED_COUNT] ?: 0 // Default to 0
        }

    suspend fun incrementQuotesShared() {
        context.dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.QUOTES_SHARED_COUNT] ?: 0
            preferences[PreferencesKeys.QUOTES_SHARED_COUNT] = currentCount + 1
        }
    }
}