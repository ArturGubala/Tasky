package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val title: String,
    val description: String?,
    val timeFrom: String,
    val timeTo: String,
    val remindAt: String,
    val updatedAt: String?,
    val hostId: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photoKeys: List<PhotoDto>,
)
