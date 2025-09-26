package com.example.tasky.core.data.database.reminder.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_delete_pending_sync")
data class ReminderDeletedSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val reminderId: String,
    val userId: String,
)
