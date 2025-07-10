// File: app/src/main/java/com/kiprono/randomquote/data/Quote.kt
package com.kiprono.randomquote.data

import com.google.gson.annotations.SerializedName

// Data class to model the structure of a quote received from ZenQuotes API
data class Quote(
    @SerializedName("q") val content: String, // 'q' is the quote content in ZenQuotes
    @SerializedName("a") val author: String,   // 'a' is the author in ZenQuotes
    @SerializedName("h") val html: String      // 'h' is the HTML version of the quote
)