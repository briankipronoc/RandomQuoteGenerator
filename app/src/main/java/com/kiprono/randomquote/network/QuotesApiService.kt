package com.kiprono.randomquote.network

import retrofit2.Response
import retrofit2.http.GET

// Data class to parse the response from ZenQuotes API
// ZenQuotes returns a list where each object has 'q' for quote and 'a' for author.
data class ZenQuoteApiResponse(
    val q: String, // Quote content
    val a: String // Author
    // You can add 'c' for character count and 'h' for HTML if needed, but not strictly required
    // val c: String? = null,
    // val h: String? = null
)

interface QuoteApiService {
    @GET("api/random") // This is the endpoint for a random quote from ZenQuotes
    suspend fun getRandomQuote(): Response<List<ZenQuoteApiResponse>> // ZenQuotes returns a list
}