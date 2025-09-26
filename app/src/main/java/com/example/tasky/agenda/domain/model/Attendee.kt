@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Attendee(
    val email: String,
    val username: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Instant,
    val isCreator: Boolean,
)
