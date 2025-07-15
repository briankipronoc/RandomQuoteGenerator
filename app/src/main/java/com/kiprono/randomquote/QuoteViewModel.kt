package com.kiprono.randomquote.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiprono.randomquote.data.Achievement
import com.kiprono.randomquote.data.AchievementDao
import com.kiprono.randomquote.data.FavoriteQuoteDao
import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.data.UserPreferencesRepository
import com.kiprono.randomquote.network.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn // Import stateIn
import kotlinx.coroutines.flow.SharingStarted // Import SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteRepository: QuoteRepository, // <--- ADDED COMMA HERE
    private val favoriteQuoteDao: FavoriteQuoteDao,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val achievementDao: AchievementDao
) : ViewModel() {

    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState: StateFlow<Quote?> = _quoteState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // User Preferences (now exposed as StateFlows using stateIn)
    val userName: StateFlow<String> = userPreferencesRepository.userName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Keep active as long as there are collectors
            initialValue = "Guest" // Initial value before DataStore emits
        )

    val quotesReadCount: StateFlow<Int> = userPreferencesRepository.quotesReadCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val favoriteQuotesCount: StateFlow<Int> = userPreferencesRepository.favoriteQuotesCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val quotesSharedCount: StateFlow<Int> = userPreferencesRepository.quotesSharedCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Favorite Quotes
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allFavorites = favoriteQuoteDao.getAllFavorites()

    val filteredFavorites = combine(
        allFavorites,
        _searchQuery
    ) { favorites, query ->
        if (query.isBlank()) {
            favorites
        } else {
            favorites.filter {
                it.text.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            }
        }
    }

    // Achievements
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        Log.d("QuoteViewModel", "ViewModel initialized by Hilt.")
        viewModelScope.launch {
            initializeAchievements()
            achievementDao.getAllAchievements().collect { dbAchievements ->
                _achievements.value = dbAchievements
            }
        }
        getRandomQuote()
    }

    // Achievement Initialization
    private suspend fun initializeAchievements() {
        val existingAchievements = achievementDao.getAllAchievements().first()
        if (existingAchievements.isEmpty()) {
            val defaultAchievements = listOf(
                Achievement(title = "First Read", description = "Read 1 quote", threshold = 1),
                Achievement(title = "Quote Enthusiast", description = "Read 10 quotes", threshold = 10),
                Achievement(title = "Quote Master", description = "Read 50 quotes", threshold = 50),
                Achievement(title = "First Favorite", description = "Add 1 quote to favorites", threshold = 1),
                Achievement(title = "Collector", description = "Add 5 quotes to favorites", threshold = 5),
                Achievement(title = "Sharer", description = "Share 1 quote", threshold = 1),
                Achievement(title = "Social Butterfly", description = "Share 5 quotes", threshold = 5)
            )
            defaultAchievements.forEach { achievementDao.insertAchievement(it) }
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val quote = quoteRepository.fetchRandomQuote()
                _quoteState.value = quote
                userPreferencesRepository.incrementQuotesRead()
                updateAchievementProgress(AchievementType.READ, 1)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch quote: ${e.message}"
                Log.e("QuoteViewModel", "Error fetching quote", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addOrRemoveFavorite() {
        viewModelScope.launch {
            _quoteState.value?.let { currentQuote ->
                val isCurrentlyFavorited = favoriteQuoteDao.isQuoteFavorited(currentQuote.text, currentQuote.author)

                if (isCurrentlyFavorited) {
                    favoriteQuoteDao.deleteFavorite(currentQuote)
                } else {
                    favoriteQuoteDao.addFavorite(currentQuote)
                    updateAchievementProgress(AchievementType.FAVORITE, 1)
                }
                userPreferencesRepository.updateFavoriteQuotesCount(favoriteQuoteDao.getAllFavorites().first().size)
            }
        }
    }

    fun removeFavorite(quote: Quote) {
        viewModelScope.launch {
            favoriteQuoteDao.deleteFavorite(quote)
            userPreferencesRepository.updateFavoriteQuotesCount(favoriteQuoteDao.getAllFavorites().first().size)
        }
    }

    fun incrementSharedQuotes() {
        viewModelScope.launch {
            userPreferencesRepository.incrementQuotesShared()
            updateAchievementProgress(AchievementType.SHARE, 1)
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserName(name)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Function to get achievement progress for display
    fun getAchievementProgress(achievement: Achievement): Int {
        // Now these are StateFlows, so .value is accessible
        return when (achievement.title) {
            "First Read", "Quote Enthusiast", "Quote Master" -> quotesReadCount.value
            "First Favorite", "Collector" -> favoriteQuotesCount.value
            "Sharer", "Social Butterfly" -> quotesSharedCount.value
            else -> 0
        }
    }

    private suspend fun updateAchievementProgress(type: AchievementType, progressIncrement: Int) {
        val currentAchievements = achievementDao.getAllAchievements().first()
        currentAchievements.forEach { achievement ->
            when (type) {
                AchievementType.READ -> {
                    if (achievement.title == "First Read" || achievement.title == "Quote Enthusiast" || achievement.title == "Quote Master") {
                        val newProgress = (achievement.currentValue + progressIncrement).coerceAtMost(achievement.threshold)
                        if (newProgress >= achievement.threshold && !achievement.unlocked) {
                            achievement.unlocked = true
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        } else if (!achievement.unlocked) {
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        }
                    }
                }
                AchievementType.FAVORITE -> {
                    if (achievement.title == "First Favorite" || achievement.title == "Collector") {
                        val newProgress = (achievement.currentValue + progressIncrement).coerceAtMost(achievement.threshold)
                        if (newProgress >= achievement.threshold && !achievement.unlocked) {
                            achievement.unlocked = true
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        } else if (!achievement.unlocked) {
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        }
                    }
                }
                AchievementType.SHARE -> {
                    if (achievement.title == "Sharer" || achievement.title == "Social Butterfly") {
                        val newProgress = (achievement.currentValue + progressIncrement).coerceAtMost(achievement.threshold)
                        if (newProgress >= achievement.threshold && !achievement.unlocked) {
                            achievement.unlocked = true
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        } else if (!achievement.unlocked) {
                            achievement.currentValue = newProgress
                            achievementDao.updateAchievement(achievement)
                        }
                    }
                }
            }
        }
    }
}

enum class AchievementType {
    READ, FAVORITE, SHARE
}