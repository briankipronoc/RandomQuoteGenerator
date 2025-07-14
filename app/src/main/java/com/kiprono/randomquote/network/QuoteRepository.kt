package com.kiprono.randomquote.network

import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.data.RetrofitClient // Ensure this import is correct

class QuoteRepository {
    private val api = RetrofitClient.quoteApiService

    // In-memory favorites list (You'll eventually want to persist this)
    private val favorites = mutableListOf<Quote>()

    suspend fun fetchRandomQuote(): Quote? {
        val response = api.getRandomQuote()
        if (response.isSuccessful) {
            val zenQuoteList = response.body() // This will be List<ZenQuoteApiResponse>
            return if (!zenQuoteList.isNullOrEmpty()) {
                val zenQuote = zenQuoteList.first() // Get the first item from the list
                // Convert ZenQuoteApiResponse to your existing Quote data class
                Quote(
                    id = 0, // <--- ADD THIS LINE to provide a default ID for a new quote
                    content = zenQuote.q, // Map 'q' from API response to 'content'
                    author = zenQuote.a // Map 'a' from API response to 'author'
                )
            } else {
                null // Empty list returned
            }
        } else {
            // Handle API error, maybe log it or throw an exception
            // For now, just return null, and ViewModel will pick up the error message.
            println("API Error: ${response.code()} - ${response.message()}")
            return null
        }
    }

    // These favorite methods in the repository might be redundant if favoriteQuoteDao
    // in the ViewModel is the primary source of truth for favorites.
    // Consider removing them if they are not used elsewhere or cause confusion.
    fun addFavorite(quote: Quote) {
        if (!favorites.contains(quote)) {
            favorites.add(quote)
        }
    }

    fun getFavorites(): List<Quote> = favorites
}