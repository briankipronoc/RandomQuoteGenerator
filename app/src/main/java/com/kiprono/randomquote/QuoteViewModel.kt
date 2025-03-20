package com.kiprono.randomquote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel to manage quotes and favorites
class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository() // Use the repository

    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState: StateFlow<Quote?> = _quoteState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getFavorites(): List<Quote> = repository.getFavorites() // Get favorites from repository

    fun addCurrentQuoteToFavorites() {
        _quoteState.value?.let { quote ->
            repository.addFavorite(quote) // Add to repository
        }
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _quoteState.value = repository.fetchRandomQuote() // Fetch from API
            _isLoading.value = false
        }
    }
}