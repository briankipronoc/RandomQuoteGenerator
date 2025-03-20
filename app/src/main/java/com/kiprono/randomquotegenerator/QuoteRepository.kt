package com.example.randomquote

class QuoteRepository {
    private val api = RetrofitInstance.api

    // In-memory favorites list
    private val favorites = mutableListOf<Quote>()

    suspend fun fetchRandomQuote(): Quote {
        return api.getRandomQuote()
    }

    fun addFavorite(quote: Quote) {
        if (!favorites.contains(quote)) {
            favorites.add(quote)
        }
    }

    fun getFavorites(): List<Quote> = favorites
}