// File: app/src/main/java/com/kiprono/randomquote/data/RetrofitInstance.kt
package com.kiprono.randomquote.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface QuoteApiService {
    @GET("random/")
    suspend fun getRandomQuote(): List<Quote>
}

object RetrofitInstance {
    private const val BASE_URL = "https://zenquotes.io/api/"

    val api: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}