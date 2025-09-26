package com.example.tasky.agenda.data.network.tasky.mappers

import com.example.tasky.agenda.data.network.event.mappers.toEvent
import com.example.tasky.agenda.data.network.reminder.mappers.toReminder
import com.example.tasky.agenda.data.network.task.mappers.toTask
import com.example.tasky.agenda.data.network.tasky.dto.AgendaDto
import com.example.tasky.agenda.domain.model.Agenda

fun AgendaDto.toAgenda(): Agenda {
    return Agenda(
        tasks = taskDtos.map { it.toTask() },
        events = eventDtos.map { it.toEvent() },
        reminders = reminderDtos.map { it.toReminder() }
    )
}
