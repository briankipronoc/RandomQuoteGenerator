// app/src/main/java/com/kiprono/randomquote/data/UserPreferencesRepository.kt

package com.kiprono.randomquote.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore keys
private val USER_NAME_KEY = stringPreferencesKey("user_name")
private val QUOTES_READ_COUNT_KEY = intPreferencesKey("quotes_read_count")
private val QUOTES_LIKED_COUNT_KEY = intPreferencesKey("quotes_liked_count") // ADDED: Key for liked count
private val QUOTES_SHARED_COUNT_KEY = intPreferencesKey("quotes_shared_count")

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

    // ADDED: Expose quotesLikedCount as a Flow
    val quotesLikedCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUOTES_LIKED_COUNT_KEY] ?: 0
    }

    // Expose quotesSharedCount as a Flow
    val quotesSharedCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[QUOTES_SHARED_COUNT_KEY] ?: 0
    }

    // Function to save the user name
    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // ADDED: Function to save quotes read count
    suspend fun saveQuotesReadCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_READ_COUNT_KEY] = count
        }
    }

    // ADDED: Function to save quotes liked count
    suspend fun saveQuotesLikedCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_LIKED_COUNT_KEY] = count
        }
    }

    // ADDED: Function to save quotes shared count
    suspend fun saveQuotesSharedCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_SHARED_COUNT_KEY] = count
        }
    }

    // The UserStats data class and readStats/saveStats functions might become redundant
    // if you exclusively use the individual Flow properties and save functions.
    // You can keep them if other parts of your app still use them.
    data class UserStats(
        val readCount: Int,
        val sharedCount: Int,
        val likedCount: Int // Added likedCount to UserStats if still used
    )

    val readStats: Flow<UserStats> = dataStore.data.map { preferences ->
        UserStats(
            readCount = preferences[QUOTES_READ_COUNT_KEY] ?: 0,
            sharedCount = preferences[QUOTES_SHARED_COUNT_KEY] ?: 0,
            likedCount = preferences[QUOTES_LIKED_COUNT_KEY] ?: 0 // Added to UserStats
        )
    }

    // Updated saveStats to include likedCount if you still use this function
    suspend fun saveStats(readCount: Int, sharedCount: Int, likedCount: Int) {
        dataStore.edit { preferences ->
            preferences[QUOTES_READ_COUNT_KEY] = readCount
            preferences[QUOTES_SHARED_COUNT_KEY] = sharedCount
            preferences[QUOTES_LIKED_COUNT_KEY] = likedCount
        }
    }
}