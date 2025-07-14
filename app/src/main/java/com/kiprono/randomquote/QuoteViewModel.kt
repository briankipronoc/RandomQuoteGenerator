package com.kiprono.randomquote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kiprono.randomquote.data.FavoriteQuoteDao
import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuoteViewModel(
    private val quoteRepository: QuoteRepository,
    private val favoriteQuoteDao: FavoriteQuoteDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState: StateFlow<Quote?> = _quoteState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _quotesReadCount = MutableStateFlow(0)
    val quotesReadCount: StateFlow<Int> = _quotesReadCount.asStateFlow()

    private val _quotesLikedCount = MutableStateFlow(0)
    val quotesLikedCount: StateFlow<Int> = _quotesLikedCount.asStateFlow()

    private val _quotesSharedCount = MutableStateFlow(0)
    val quotesSharedCount: StateFlow<Int> = _quotesSharedCount.asStateFlow()

    private val _allFavorites = MutableStateFlow<List<Quote>>(emptyList())
    val allFavorites: StateFlow<List<Quote>> = _allFavorites.asStateFlow()

    // Initialize with a random quote and user preferences
    init {
        Log.d("QuoteViewModel", "ViewModel initialized. Attempting to load initial data.")
        loadUserPreferences()
        observeFavorites()
        getRandomQuote() // Initial quote fetch
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            Log.d("QuoteViewModel", "Loading user preferences...")
            userPreferencesRepository.userName.collect { name ->
                _userName.value = name
                Log.d("QuoteViewModel", "Loaded userName: $name")
            }
            userPreferencesRepository.quotesReadCount.collect { count ->
                _quotesReadCount.value = count
                Log.d("QuoteViewModel", "Loaded quotesReadCount: $count")
            }
            userPreferencesRepository.quotesLikedCount.collect { count ->
                _quotesLikedCount.value = count
                Log.d("QuoteViewModel", "Loaded quotesLikedCount: $count")
            }
            userPreferencesRepository.quotesSharedCount.collect { count ->
                _quotesSharedCount.value = count
                Log.d("QuoteViewModel", "Loaded quotesSharedCount: $count")
            }
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Clear previous errors
            Log.d("QuoteViewModel", "Attempting to fetch random quote...")
            try {
                val quote = quoteRepository.fetchRandomQuote()
                if (quote != null) {
                    _quoteState.value = quote
                    incrementQuotesRead() // Increment read count on successful fetch
                    Log.d("QuoteViewModel", "Successfully fetched quote: \"${quote.content}\" by ${quote.author}")
                } else {
                    _quoteState.value = null
                    _errorMessage.value = "No quote found or API returned empty response."
                    Log.w("QuoteViewModel", "fetchRandomQuote returned null or empty.")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load quote: ${e.localizedMessage ?: "Unknown error"}"
                _quoteState.value = null
                Log.e("QuoteViewModel", "Error fetching quote: ${e.message}", e)
            } finally {
                _isLoading.value = false
                Log.d("QuoteViewModel", "Finished quote fetch attempt. isLoading: ${_isLoading.value}")
            }
        }
    }

    private fun incrementQuotesRead() {
        viewModelScope.launch {
            val currentCount = _quotesReadCount.value
            userPreferencesRepository.saveQuotesReadCount(currentCount + 1)
            Log.d("QuoteViewModel", "Quotes Read Count incremented to ${currentCount + 1}")
        }
    }

    fun addCurrentQuoteToFavorites() {
        viewModelScope.launch {
            _quoteState.value?.let { currentQuote ->
                val existingFavorite = favoriteQuoteDao.getFavoriteByContentAndAuthor(currentQuote.content, currentQuote.author)
                if (existingFavorite == null) {
                    favoriteQuoteDao.insert(currentQuote)
                    incrementQuotesLiked() // Increment liked count
                    Log.d("QuoteViewModel", "Added to favorites: ${currentQuote.content}")
                } else {
                    favoriteQuoteDao.delete(existingFavorite)
                    decrementQuotesLiked() // Decrement liked count if un-favoriting
                    Log.d("QuoteViewModel", "Removed from favorites: ${currentQuote.content}")
                }
            } ?: Log.w("QuoteViewModel", "Attempted to favorite null quote.")
        }
    }

    fun removeFavorite(quote: Quote) {
        viewModelScope.launch {
            favoriteQuoteDao.delete(quote)
            decrementQuotesLiked()
            Log.d("QuoteViewModel", "Removed favorite via profile screen: ${quote.content}")
        }
    }

    private fun incrementQuotesLiked() {
        viewModelScope.launch {
            val currentCount = _quotesLikedCount.value
            userPreferencesRepository.saveQuotesLikedCount(currentCount + 1)
            Log.d("QuoteViewModel", "Quotes Liked Count incremented to ${currentCount + 1}")
        }
    }

    private fun decrementQuotesLiked() {
        viewModelScope.launch {
            val currentCount = _quotesLikedCount.value
            userPreferencesRepository.saveQuotesLikedCount(maxOf(0, currentCount - 1)) // Prevent negative count
            Log.d("QuoteViewModel", "Quotes Liked Count decremented to ${maxOf(0, currentCount - 1)}")
        }
    }

    fun incrementSharedQuotes() {
        viewModelScope.launch {
            val currentCount = _quotesSharedCount.value
            userPreferencesRepository.saveQuotesSharedCount(currentCount + 1)
            Log.d("QuoteViewModel", "Quotes Shared Count incremented to ${currentCount + 1}")
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserName(name)
            Log.d("QuoteViewModel", "Saved userName: $name")
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteQuoteDao.getAllFavorites().collect { favorites ->
                _allFavorites.value = favorites
                Log.d("QuoteViewModel", "Favorites updated: ${favorites.size} items")
            }
        }
    }
}


class QuoteViewModelFactory(
    private val favoriteQuoteDao: FavoriteQuoteDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val quoteRepository = QuoteRepository() // Instantiate QuoteRepository here
            return QuoteViewModel(
                quoteRepository = quoteRepository,
                favoriteQuoteDao = favoriteQuoteDao,
                userPreferencesRepository = userPreferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}