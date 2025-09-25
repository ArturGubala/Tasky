package com.example.tasky.agenda.domain.util

enum class AgendaItemType {
    TASK,
    EVENT,
    REMINDER
}

fun AgendaItemType.toInt(): Int = when (this) {
    AgendaItemType.TASK -> 0
    AgendaItemType.EVENT -> 1
    AgendaItemType.REMINDER -> 2
}

fun Int.toAgendaItemType(): AgendaItemType? = when (this) {
    0 -> AgendaItemType.TASK
    1 -> AgendaItemType.EVENT
    2 -> AgendaItemType.REMINDER
    else -> null
}
