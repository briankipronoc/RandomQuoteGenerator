package com.kiprono.randomquote.network

import com.kiprono.randomquote.data.Quote
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val apiService: QuoteApiService
) {
    suspend fun fetchRandomQuote(): Quote {
        return try {
            val quotes = apiService.getRandomQuote()
            // Zen Quotes API returns a list, take the first one
            quotes.firstOrNull() ?: throw Exception("No quote received from API")
        } catch (e: Exception) {
            // Log the error for debugging
            e.printStackTrace()
            // Provide a fallback Quote object
            Quote(
                id = 0, // Default ID for Room
                text = "Failed to load quote. Please check your internet connection.",
                author = "System"
            )
        }
    }
}