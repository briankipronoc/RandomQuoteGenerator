package com.kiprono.randomquote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kiprono.randomquote.data.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository()

    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState: StateFlow<Quote?> = _quoteState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _favorites = MutableStateFlow<List<Quote>>(emptyList())
    val favorites: StateFlow<List<Quote>> = _favorites

    init {
        getRandomQuote()
        _favorites.value = repository.getFavorites()
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val quotes = repository.fetchRandomQuote()
                if (quotes.isNotEmpty()) {
                    _quoteState.value = quotes.first()
                } else {
                    _errorMessage.value = "No quote received."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch quote: ${e.localizedMessage ?: "Unknown error"}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCurrentQuoteToFavorites() {
        _quoteState.value?.let { quote ->
            repository.addFavorite(quote)
            _favorites.value = repository.getFavorites()
        }
    }
}