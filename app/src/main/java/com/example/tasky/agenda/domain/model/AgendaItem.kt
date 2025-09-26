package com.example.tasky.agenda.domain.model

import com.example.tasky.agenda.domain.util.AgendaKind

sealed class AgendaItem(open val id: String) {
    data class Task(override val id: String) : AgendaItem(id)
    data class Reminder(override val id: String) : AgendaItem(id)
    data class Event(override val id: String) : AgendaItem(id)
}

fun AgendaItem.kind(): AgendaKind = when (this) {
    is AgendaItem.Task -> AgendaKind.TASK
    is AgendaItem.Reminder -> AgendaKind.REMINDER
    is AgendaItem.Event -> AgendaKind.EVENT
}
