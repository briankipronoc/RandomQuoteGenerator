package com.kiprono.randomquote

import retrofit2.http.GET

interface QuoteApi {
    @GET("random")
    suspend fun getRandomQuote(): Quote
}