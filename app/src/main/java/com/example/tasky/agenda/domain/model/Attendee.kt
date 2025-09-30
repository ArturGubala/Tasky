@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface AttendeeMinimal {
    val userId: String
    val email: String
    val name: String
}

data class LookupAttendee(
    override val userId: String,
    override val email: String,
    override val name: String,
) : AttendeeMinimal

// From event payload (full attendee)
data class EventAttendee(
    override val userId: String,
    override val email: String,
    override val name: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Instant,
    val isCreator: Boolean,
) : AttendeeMinimal
