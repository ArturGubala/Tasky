package com.example.tasky.core.data.database.reminder.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val updatedAt: Long?,
)
