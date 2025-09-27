@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.util.AgendaKind
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

sealed interface AgendaItemUi {
    val id: String
    val time: ZonedDateTime
    val title: String
    val description: String?
    val agendaKind: AgendaKind
    val isCompletable: Boolean get() = false
    val isCompleted: Boolean get() = false
    val toTime: ZonedDateTime? get() = null

    data class TaskItem(val task: Task) : AgendaItemUi {
        override val id: String = task.id
        override val time: ZonedDateTime =
            ZonedDateTime.ofInstant(task.time.toJavaInstant(), ZoneId.systemDefault())
        override val title: String = task.title
        override val description: String? = task.description
        override val agendaKind: AgendaKind = AgendaKind.TASK
        override val isCompletable: Boolean = true
        override val isCompleted: Boolean = task.isDone
    }

    data class EventItem(val event: Event) : AgendaItemUi {
        override val id: String = event.id
        override val time: ZonedDateTime =
            ZonedDateTime.ofInstant(event.timeFrom.toJavaInstant(), ZoneId.systemDefault())
        override val title: String = event.title
        override val description: String? = event.description
        override val agendaKind: AgendaKind = AgendaKind.EVENT
        override val toTime: ZonedDateTime =
            ZonedDateTime.ofInstant(event.timeTo.toJavaInstant(), ZoneId.systemDefault())
    }

    data class ReminderItem(val reminder: Reminder) : AgendaItemUi {
        override val id: String = reminder.id
        override val time: ZonedDateTime =
            ZonedDateTime.ofInstant(reminder.time.toJavaInstant(), ZoneId.systemDefault())
        override val title: String = reminder.title
        override val description: String? = reminder.description
        override val agendaKind: AgendaKind = AgendaKind.REMINDER
    }
}
