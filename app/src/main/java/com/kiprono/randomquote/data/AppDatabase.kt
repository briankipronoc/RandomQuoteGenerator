package com.kiprono.randomquote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Increment the version number
@Database(entities = [Quote::class], version = 3, exportSchema = false) // <-- Changed to version = 3
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteQuoteDao(): FavoriteQuoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorite_quote_database"
                )
                    // Uncomment or ensure this line is present for development
                    .fallbackToDestructiveMigration() // <-- Make sure this line is active
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}