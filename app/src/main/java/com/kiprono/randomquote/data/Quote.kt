// app/src/main/java/com/kiprono/randomquote/data/Quote.kt

package com.kiprono.randomquote.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Data class for a quote from the API (used for Retrofit parsing)
data class NetworkQuote(
    @SerializedName("_id") val id: String,
    val content: String?,
    val author: String?,
    val tags: List<String>?,
    val authorSlug: String?,
    val length: Int?,
    val dateAdded: String?,
    val dateModified: String?
)

// Data class for a quote used internally and as a Room database entity.
// This is likely what FavoriteQuoteEntity was meant to be.
@Entity(tableName = "favorite_quotes") // **This makes it a Room entity**
data class Quote(
    @PrimaryKey // **Designate a primary key**
    @SerializedName("_id") val id: String, // Keep ID for uniqueness
    val content: String?,
    val author: String?,
    // Add other fields you want to persist in the database
    // For example, if you want to store tags, you'd need a TypeConverter
    // val tags: String? // Storing tags as a simple string for Room, or use TypeConverter
)

// Extension function to convert NetworkQuote to Quote for Room/internal use
fun NetworkQuote.toQuote(): Quote {
    return Quote(
        id = this.id,
        content = this.content,
        author = this.author
    )
}