// Example: app/src/main/java/com/kiprono/randomquote/data/QuoteApiService.kt
package com.kiprono.randomquote.data

import retrofit2.http.GET

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): List<Quote> // This is what the error says it currently is
    // If you intended it to be a single Quote, you could change this to:
    // suspend fun getRandomQuote(): Quote
    // But for now, we adapt the repository to match the API service.
}