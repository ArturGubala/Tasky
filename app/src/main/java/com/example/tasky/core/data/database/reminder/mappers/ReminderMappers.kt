@file:OptIn(ExperimentalTime::class)

package com.example.tasky.core.data.database.reminder.mappers

import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.data.database.reminder.entity.ReminderEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        time = Instant.fromEpochMilliseconds(time),
        remindAt = Instant.fromEpochMilliseconds(remindAt),
        updatedAt = updatedAt?.let { Instant.fromEpochMilliseconds(updatedAt) },
    )
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        time = time.toEpochMilliseconds(),
        remindAt = remindAt.toEpochMilliseconds(),
        updatedAt = updatedAt?.toEpochMilliseconds(),
    )
}
