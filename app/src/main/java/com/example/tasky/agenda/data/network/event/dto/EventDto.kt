package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val title: String,
    val description: String?,
    val from: String,
    val to: String,
    val remindAt: String? = null,
    val updatedAt: String? = null,
    val hostId: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photoKeys: List<PhotoDto>,
)
