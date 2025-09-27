package com.example.tasky.core.data.database.event.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.core.data.database.SyncOperation

@Entity(tableName = "event_upsert_pending_sync")
data class EventPendingSyncEntity(
    @Embedded val event: EventEntity,
    @PrimaryKey(autoGenerate = false)
    val eventId: String = event.id,
    val operation: SyncOperation,
    val userId: String,
)
