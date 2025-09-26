package com.example.tasky.agenda.data.network.reminder.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReminderDto(
    val id: String,
    val title: String,
    val description: String?,
    val time: String,
    val remindAt: String,
    val updatedAt: String? = null,
)
