@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Event(
    val id: String,
    val title: String,
    val description: String?,
    val timeFrom: Instant,
    val timeTo: Instant,
    val remindAt: Instant,
    val updatedAt: Instant?,
    val hostId: String,
    val isUserEventCreator: Boolean,
    val lookupAttendees: List<LookupAttendee>,
    val eventAttendees: List<EventAttendee>,
    val photos: List<Photo>,
)
