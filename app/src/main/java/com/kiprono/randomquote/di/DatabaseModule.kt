package com.kiprono.randomquote.di

import android.content.Context
import androidx.room.Room
import com.kiprono.randomquote.data.AppDatabase // You will need to create this class if you haven't
import com.kiprono.randomquote.data.AchievementDao
import com.kiprono.randomquote.data.FavoriteQuoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "random_quote_database" // Name of your database file
        )
            .fallbackToDestructiveMigration() // Handle migrations by recreating the db
            .build()
    }

    @Provides
    @Singleton
    fun provideAchievementDao(database: AppDatabase): AchievementDao {
        return database.achievementDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteQuoteDao(database: AppDatabase): FavoriteQuoteDao {
        return database.favoriteQuoteDao()
    }
}