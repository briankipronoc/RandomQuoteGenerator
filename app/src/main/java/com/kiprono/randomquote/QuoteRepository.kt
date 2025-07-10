// File: app/src/main/java/com/kiprono/randomquote/QuoteRepository.kt
package com.kiprono.randomquote

import com.kiprono.randomquote.data.Quote // Make sure this import is correct based on your Quote.kt package
import com.kiprono.randomquote.data.RetrofitInstance // Make sure this import is correct

class QuoteRepository {
    private val api = RetrofitInstance.api

    // In-memory favorites list
    private val favorites = mutableListOf<Quote>()

    // Changed return type from Quote to List<Quote>
    suspend fun fetchRandomQuote(): List<Quote> {
        return api.getRandomQuote()
    }

    fun addFavorite(quote: Quote) {
        if (!favorites.contains(quote)) {
            favorites.add(quote)
        }
    }

    fun getFavorites(): List<Quote> = favorites
}