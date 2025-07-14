// app/src/main/java/com/kiprono/randomquote/data/RetrofitInstance.kt

package com.kiprono.randomquote.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // For readTimeout and connectTimeout

object RetrofitClient {

    private const val BASE_URL = "https://api.quotable.io/" // Ensure this is correct

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // This logs headers and body for requests and responses
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Add the logging interceptor
        .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
        .build()

    val quoteApiService: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Attach the configured OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}