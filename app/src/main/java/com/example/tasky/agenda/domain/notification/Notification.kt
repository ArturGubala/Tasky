@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.domain.notification

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.util.AgendaKind
import kotlin.time.ExperimentalTime

data class Notification(
    val id: String,
    val title: String,
    val description: String?,
    val remindAt: Long,
    val kind: AgendaKind,
)

fun Task.toNotification(): Notification {
    return Notification(
        id = id,
        title = title,
        description = description,
        remindAt = remindAt.toEpochMilliseconds(),
        kind = AgendaKind.TASK
    )
}

fun Event.toNotification(): Notification {
    return Notification(
        id = id,
        title = title,
        description = description,
        remindAt = remindAt.toEpochMilliseconds(),
        kind = AgendaKind.EVENT
    )
}

fun Reminder.toNotification(): Notification {
    return Notification(
        id = id,
        title = title,
        description = description,
        remindAt = remindAt.toEpochMilliseconds(),
        kind = AgendaKind.REMINDER
    )
}
