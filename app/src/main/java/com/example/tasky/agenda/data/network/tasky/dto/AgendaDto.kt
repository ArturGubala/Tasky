package com.example.tasky.agenda.data.network.tasky.dto

import com.example.tasky.agenda.data.network.event.dto.EventDto
import com.example.tasky.agenda.data.network.reminder.dto.ReminderDto
import com.example.tasky.agenda.data.network.task.dto.TaskDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgendaDto(
    @SerialName("events")
    val eventDtos: List<EventDto>,
    @SerialName("tasks")
    val taskDtos: List<TaskDto>,
    @SerialName("reminders")
    val reminderDtos: List<ReminderDto>,
)
