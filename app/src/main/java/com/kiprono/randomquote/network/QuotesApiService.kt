package com.kiprono.randomquote.network

import com.kiprono.randomquote.data.Quote
import retrofit2.http.GET

interface QuoteApiService {
    @GET("random") // Zen Quotes API endpoint for a random quote
    suspend fun getRandomQuote(): List<Quote> // Zen Quotes returns a list, even for one quote
}