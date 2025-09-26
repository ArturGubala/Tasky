@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.data.network.reminder.mappers

import com.example.tasky.agenda.data.network.reminder.dto.ReminderDto
import com.example.tasky.agenda.domain.model.Reminder
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun ReminderDto.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        time = Instant.parse(time),
        remindAt = Instant.parse(remindAt),
        updatedAt = updatedAt?.let { Instant.parse(it) }
    )
}
