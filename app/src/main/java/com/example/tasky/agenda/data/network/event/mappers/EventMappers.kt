@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.event.mappers

import com.example.tasky.agenda.data.network.event.dto.AttendeeDto
import com.example.tasky.agenda.data.network.event.dto.CreateEventRequest
import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.event.dto.GetAttendeeResponseDto
import com.example.tasky.agenda.data.network.event.dto.PhotoDto
import com.example.tasky.agenda.data.network.event.dto.UpdateEventRequest
import com.example.tasky.agenda.data.network.event.dto.UpsertEventResponseDto
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.EventAttendee
import com.example.tasky.agenda.domain.model.LookupAttendee
import com.example.tasky.agenda.domain.model.Photo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun EventDto.toEvent(userId: String = ""): Event {
    val attendeeRemindAt = attendees.firstOrNull { it.userId == userId }?.remindAt
        ?: from

    return Event(
        id = id,
        title = title,
        description = description,
        timeFrom = Instant.parse(from),
        timeTo = Instant.parse(to),
        remindAt = remindAt?.let { Instant.parse(remindAt) } ?: Instant.parse(attendeeRemindAt),
        updatedAt = updatedAt?.let { Instant.parse(it) },
        hostId = hostId,
        isUserEventCreator = isUserEventCreator,
        lookupAttendees = listOf(),
        eventAttendees = attendees.map { it.toEventAttendee(hostId = hostId) },
        photos = photoKeys.map { it.toPhoto() },
        newPhotosIds = emptyList(),
        deletedPhotosIds = emptyList()
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
        lookupAttendees = listOf(),
        eventAttendees = event.attendees.map { it.toEventAttendee(hostId = event.hostId) },
        photos = emptyList(),
        newPhotosIds = emptyList(),
        deletedPhotosIds = emptyList()
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
        attendeeIds = lookupAttendees.map { it.userId },
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
        attendeeIds = listOf(lookupAttendees, eventAttendees.filterNot { it.isCreator })
            .flatten().map { it.userId },
        newPhotoKeys = newPhotosIds,
        deletedPhotoKeys = deletedPhotosIds,
        isGoing = true,
    )
}

fun AttendeeDto.toEventAttendee(hostId: String = ""): EventAttendee {
    return EventAttendee(
        email = email,
        name = username,
        userId = userId,
        eventId = eventId,
        isGoing = isGoing,
        remindAt = Instant.parse(remindAt),
        isCreator = hostId == userId
    )
}

fun GetAttendeeResponseDto.toLookupAttendee(): LookupAttendee {
    return LookupAttendee(
        email = email,
        name = fullName,
        userId = userId,
    )
}

fun PhotoDto.toPhoto(): Photo {
    return Photo(
        id = key,
        uri = url,
        compressedBytes = null
    )
}
