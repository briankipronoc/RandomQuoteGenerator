// File: app/src/main/java/com/kiprono/randomquote/QuoteRepository.kt
package com.kiprono.randomquote

import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.data.RetrofitClient

class QuoteRepository {
    private val api = RetrofitClient.quoteApiService

    // In-memory favorites list (You'll eventually want to persist this)
    private val favorites = mutableListOf<Quote>()

    // FIX: Modified return type to Quote? and extracted the first item from the list
    suspend fun fetchRandomQuote(): Quote? {
        // Assuming api.getRandomQuote() returns List<Quote> as per your error message.
        // We take the first item, or null if the list is empty.
        return api.getRandomQuote().firstOrNull()
    }

    fun addFavorite(quote: Quote) {
        if (!favorites.contains(quote)) {
            favorites.add(quote)
        }
    }

    fun getFavorites(): List<Quote> = favorites
}