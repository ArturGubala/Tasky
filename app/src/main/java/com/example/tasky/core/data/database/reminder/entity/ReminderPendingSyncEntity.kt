package com.example.tasky.core.data.database.reminder.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.core.data.database.SyncOperation

@Entity(tableName = "reminder_upsert_pending_sync")
data class ReminderPendingSyncEntity(
    @Embedded val reminder: ReminderEntity,
    @PrimaryKey(autoGenerate = false)
    val reminderId: String = reminder.id,
    val operation: SyncOperation,
    val userId: String,
)
