@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Reminder(
    val id: String,
    val title: String,
    val description: String?,
    val time: Instant,
    val remindAt: Instant,
    val updatedAt: Instant,
    val isDone: Boolean,
)
