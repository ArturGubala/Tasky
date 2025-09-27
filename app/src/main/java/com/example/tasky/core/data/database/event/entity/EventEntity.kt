package com.example.tasky.core.data.database.event.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String?,
    val timeFrom: Long,
    val timeTo: Long,
    val remindAt: Long,
    val updatedAt: Long?,
    val hostId: String,
    val isUserEventCreator: Boolean,
)
