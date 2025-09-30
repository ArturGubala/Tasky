@file:OptIn(ExperimentalTime::class)

package com.example.tasky.core.data.database.event.mappers

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.EventAttendee
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.core.data.database.event.entity.AttendeeEntity
import com.example.tasky.core.data.database.event.entity.EventEntity
import com.example.tasky.core.data.database.event.entity.PhotoEntity
import com.example.tasky.core.data.database.event.util.EventWithRelations
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
        lookupAttendees = listOf(),
        eventAttendees = listOf(),
        photos = listOf(),
        newPhotosIds = listOf(),
        deletedPhotosIds = listOf(),
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

fun EventWithRelations.toEvent(): Event {
    return Event(
        id = event.id,
        title = event.title,
        description = event.description,
        timeFrom = Instant.fromEpochMilliseconds(event.timeFrom),
        timeTo = Instant.fromEpochMilliseconds(event.timeTo),
        remindAt = Instant.fromEpochMilliseconds(event.remindAt),
        updatedAt = event.updatedAt?.let { Instant.fromEpochMilliseconds(it) },
        hostId = event.hostId,
        isUserEventCreator = event.isUserEventCreator,
        lookupAttendees = listOf(),
        eventAttendees = attendees.map { it.toAttendee() },
        photos = photos.map { it.toPhoto() },
        newPhotosIds = listOf(),
        deletedPhotosIds = listOf(),
    )
}

fun AttendeeEntity.toAttendee(): EventAttendee {
    return EventAttendee(
        email = email,
        name = username,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = Instant.fromEpochMilliseconds(remindAt),
        isCreator = isCreator
    )
}

fun Event.toAttendeeEntities(): List<AttendeeEntity> {
    return eventAttendees.map { attendee ->
        AttendeeEntity(
            userId = attendee.userId,
            eventId = id,
            email = attendee.email,
            username = attendee.name,
            isGoing = attendee.isGoing,
            remindAt = attendee.remindAt.toEpochMilliseconds(),
            isCreator = attendee.isCreator
        )
    }
}

fun PhotoEntity.toPhoto(): Photo {
    return Photo(
        id = key,
        uri = url,
        compressedBytes = null
    )
}

fun Event.toPhotoEntities(): List<PhotoEntity> {
    return photos.map { photo ->
        PhotoEntity(
            key = photo.id,
            eventId = id,
            url = photo.uri
        )
    }
}
