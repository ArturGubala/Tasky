package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    val id: String,
    val title: String,
    val description: String?,
    val from: String,
    val to: String,
    val remindAt: String,
    val updatedAt: String?,
    val attendeeIds: List<String>,
    val photoKeys: List<String>,
)
