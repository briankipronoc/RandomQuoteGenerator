package com.kiprono.randomquote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements") // Mark as Room Entity
data class Achievement(
    @PrimaryKey(autoGenerate = true) // Define 'id' as primary key, auto-generated
    val id: Int = 0,
    val title: String,
    val description: String,
    val threshold: Int, // The number of actions required to unlock
    var currentValue: Int = 0, // Current progress towards the threshold
    var unlocked: Boolean = false // Whether the achievement is unlocked
)