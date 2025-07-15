// com.kiprono.randomquote.data.AchievementDao.kt
package com.kiprono.randomquote.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement)

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    // Query to get all achievements, ordered for consistent display
    @Query("SELECT * FROM achievements ORDER BY id ASC")
    fun getAllAchievements(): Flow<List<Achievement>>

    // Query to get a specific achievement by its ID (if needed, adjust if using title as unique)
    @Query("SELECT * FROM achievements WHERE id = :achievementId LIMIT 1")
    suspend fun getAchievementById(achievementId: Int): Achievement?

    // Query to delete all achievements (useful for testing or resetting progress)
    @Query("DELETE FROM achievements")
    suspend fun deleteAllAchievements()
}