package com.example.tasky.core.data.database.event.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "attendee",
    primaryKeys = ["userId", "eventId"],  // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["eventId"])]
)
data class AttendeeEntity(
    val userId: String,
    val eventId: String,
    val email: String,
    val username: String,
    val isGoing: Boolean,
    val remindAt: Long,
    val isCreator: Boolean,
)
