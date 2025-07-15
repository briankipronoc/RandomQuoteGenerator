package com.kiprono.randomquote.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quote::class, Achievement::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteQuoteDao(): FavoriteQuoteDao
    abstract fun achievementDao(): AchievementDao
}