@file:OptIn(ExperimentalTime::class)

package com.example.tasky.core.data.database.event.mappers

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.core.data.database.event.entity.EventEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun EventEntity.toEvent(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        timeFrom = Instant.fromEpochMilliseconds(timeFrom),
        timeTo = Instant.fromEpochMilliseconds(timeTo),
        remindAt = Instant.fromEpochMilliseconds(remindAt),
        updatedAt = updatedAt?.let { Instant.fromEpochMilliseconds(updatedAt) },
        hostId = hostId,
        isUserEventCreator = isUserEventCreator,
        attendees = listOf(),
        photos = listOf()
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        description = description,
        timeFrom = timeFrom.toEpochMilliseconds(),
        timeTo = timeTo.toEpochMilliseconds(),
        remindAt = remindAt.toEpochMilliseconds(),
        updatedAt = updatedAt?.toEpochMilliseconds(),
        hostId = hostId,
        isUserEventCreator = isUserEventCreator
    )
}
