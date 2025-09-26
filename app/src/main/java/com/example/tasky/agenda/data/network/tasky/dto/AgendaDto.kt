package com.example.tasky.agenda.data.network.tasky.dto

import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.reminder.dto.ReminderDto
import com.example.tasky.agenda.data.network.task.dto.TaskDto
import kotlinx.serialization.Serializable

@Serializable
data class AgendaDto(
    val eventDtos: List<EventDto>,
    val taskDtos: List<TaskDto>,
    val reminderDtos: List<ReminderDto>,
)
