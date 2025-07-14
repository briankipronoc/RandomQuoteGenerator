package com.kiprono.randomquote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define Quote as a Room Entity
@Entity(tableName = "favorite_quotes") // <--- ENSURE THIS TABLE NAME MATCHES THE DAO QUERIES
data class Quote(
    @PrimaryKey(autoGenerate = true) // Primary key, auto-generated for new entries
    val id: Int = 0, // Default value 0 for auto-generated IDs
    val content: String?, // Quote content, made nullable to match API/data source
    val author: String? // Author, made nullable to match API/data source
)