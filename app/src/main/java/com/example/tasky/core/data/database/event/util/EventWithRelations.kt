package com.example.tasky.core.data.database.event.util

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tasky.core.data.database.event.entity.AttendeeEntity
import com.example.tasky.core.data.database.event.entity.EventEntity
import com.example.tasky.core.data.database.event.entity.PhotoEntity

data class EventWithRelations(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val attendees: List<AttendeeEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "eventId"
    )
    val photos: List<PhotoEntity>,
)
