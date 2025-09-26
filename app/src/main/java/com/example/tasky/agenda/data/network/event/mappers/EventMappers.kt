@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.event.mappers

import com.example.tasky.agenda.data.network.event.dto.AttendeeDto
import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.event.dto.PhotoDto
import com.example.tasky.agenda.domain.model.Attendee
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Photo
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun EventDto.toEvent(): Event {
    return Event(
        id = id,
        title = title,
        description = description,
        timeFrom = Instant.parse(timeFrom),
        timeTo = Instant.parse(timeTo),
        remindAt = Instant.parse(remindAt),
        updatedAt = updatedAt?.let { Instant.parse(it) },
        hostId = hostId,
        isUserEventCreator = isUserEventCreator,
        attendees = attendees.map { it.toAttendee(hostId = hostId) },
        photos = photoKeys.map { it.toPhoto() }
    )
}

fun AttendeeDto.toAttendee(hostId: String): Attendee {
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

fun PhotoDto.toPhoto(): Photo {
    return Photo(
        id = key,
        uri = url,
        compressedBytes = null
    )
}
