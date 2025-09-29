@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.event.mappers

import com.example.tasky.agenda.data.network.event.dto.AttendeeDto
import com.example.tasky.agenda.data.network.event.dto.CreateEventRequest
import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.event.dto.GetAttendeeResponseDto
import com.example.tasky.agenda.data.network.event.dto.PhotoDto
import com.example.tasky.agenda.data.network.event.dto.UpdateEventRequest
import com.example.tasky.agenda.data.network.event.dto.UpsertEventResponseDto
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Photo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun EventDto.toEvent(): Event {
    val remindAt = attendees.firstOrNull()?.remindAt
        ?: from

    return Event(
        id = id,
        title = title,
        description = description,
        timeFrom = Instant.parse(from),
        timeTo = Instant.parse(to),
        remindAt = Instant.parse(remindAt),
        updatedAt = updatedAt?.let { Instant.parse(it) },
        hostId = hostId,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendee(hostId = hostId) },
        photos = photoKeys.map { it.toPhoto() }
    )
}

fun UpsertEventResponseDto.toEvent(): Event {
    return Event(
        id = event.id,
        title = event.title,
        description = event.description,
        timeFrom = Instant.parse(event.from),
        timeTo = Instant.parse(event.to),
        remindAt = event.remindAt?.let { Instant.parse(it) }
            ?: Instant.parse(event.from),
        updatedAt = event.updatedAt?.let { Instant.parse(it) },
        hostId = event.hostId,
        isUserEventCreator = event.isUserEventCreator,
        attendees = emptyList(),
        photos = emptyList()
    )
}

fun Event.toCreateEventRequest(): CreateEventRequest {
    return CreateEventRequest(
        id = id,
        title = title,
        description = description,
        from = timeFrom.toString(),
        to = timeTo.toString(),
        remindAt = remindAt.toString(),
        updatedAt = updatedAt?.toString(),
        attendeeIds = attendees.map { it.userId },
        photoKeys = photos.map { it.id }
    )
}

fun Event.toUpdateEventRequest(): UpdateEventRequest {
    return UpdateEventRequest(
        id = id,
        title = title,
        description = description,
        from = timeFrom.toString(),
        to = timeTo.toString(),
        remindAt = remindAt.toString(),
        updatedAt = updatedAt?.toString(),
        attendeeIds = attendees.map { it.userId },
        newPhotoKeys = listOf(),
        deletedPhotoKeys = listOf(),
        isGoing = true,
    )
}

fun AttendeeDto.toAttendee(hostId: String = ""): Attendee {
    return Attendee(
        email = email,
        username = username,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = Instant.parse(remindAt),
        isCreator = hostId == userId
    )
}

fun GetAttendeeResponseDto.toAttendee(): Attendee {
    return Attendee(
        email = email,
        username = fullName,
        userId = userId,
        eventId = "",
        isGoing = false,
        remindAt = Clock.System.now(),
        isCreator = false
    )
}

fun PhotoDto.toPhoto(): Photo {
    return Photo(
        id = key,
        uri = url,
        compressedBytes = null
    )
}
