package com.kiprono.randomquote.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete // Import Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteQuoteDao {

    @Query("SELECT * FROM favorite_quotes ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(quote: Quote)

    @Delete // Use @Delete for removing an entity
    suspend fun deleteFavorite(quote: Quote) // Renamed from removeFavorite for clarity and convention

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_quotes WHERE text = :quoteText AND author = :quoteAuthor LIMIT 1)")
    suspend fun isQuoteFavorited(quoteText: String, quoteAuthor: String): Boolean
}