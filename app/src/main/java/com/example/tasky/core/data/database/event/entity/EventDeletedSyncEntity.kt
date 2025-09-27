package com.example.tasky.core.data.database.event.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_delete_pending_sync")
data class EventDeletedSyncEntity(
    @PrimaryKey(autoGenerate = false)
    val eventId: String,
    val userId: String,
)
