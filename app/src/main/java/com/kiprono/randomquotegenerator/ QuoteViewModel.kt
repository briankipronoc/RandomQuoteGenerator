package com.example.randomquote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository()

    // StateFlow for the current quote and loading state
    private val _quoteState = MutableStateFlow<Quote?>(null)
    val quoteState: StateFlow<Quote?> = _quoteState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getRandomQuote()
    }

    fun getRandomQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _quoteState.value = repository.fetchRandomQuote()
            } catch (e: Exception) {
                // Handle error (e.g., update an error state)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCurrentQuoteToFavorites() {
        _quoteState.value?.let { repository.addFavorite(it) }
    }

    fun getFavorites(): List<Quote> = repository.getFavorites()
}