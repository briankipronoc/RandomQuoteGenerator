package com.kiprono.randomquote.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("q") // For Zen Quotes API: maps "q" to text
    val text: String,
    @SerializedName("a") // For Zen Quotes API: maps "a" to author
    val author: String,
    val category: String? = null // Optional, depending on your API response
)