// app/src/main/java/com/kiprono/randomquote/data/FavoriteQuoteDao.kt

package com.kiprono.randomquote.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteQuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote) // **Ensure it takes a Quote object**

    @Delete
    suspend fun delete(quote: Quote) // **Ensure it takes a Quote object**

    @Query("SELECT * FROM favorite_quotes ORDER BY author ASC")
    fun getAllFavorites(): Flow<List<Quote>> // **Ensure it returns a List of Quote objects**

    @Query("SELECT * FROM favorite_quotes WHERE content = :content AND author = :author LIMIT 1")
    suspend fun getFavoriteByContentAndAuthor(content: String?, author: String?): Quote? // **Ensure it returns a nullable Quote**
}