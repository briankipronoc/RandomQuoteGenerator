package com.kiprono.randomquote.di

import android.content.Context
import com.kiprono.randomquote.data.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This module's bindings will live as long as the application
object AppModule {

    @Provides
    @Singleton // Ensure only one instance of UserPreferencesRepository is created throughout the app's lifetime
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context // Hilt automatically provides the Application Context here
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}