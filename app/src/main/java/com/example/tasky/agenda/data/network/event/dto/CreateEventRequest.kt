package com.example.tasky.agenda.data.network.event.dto

data class CreateEventRequest(
    val id: String,
    val title: String,
    val description: String?,
    val timeFrom: String,
    val timeTo: String,
    val remindAt: String,
    val updatedAt: String?,
    val attendees: List<String>,
    val photoKeys: List<String>,
)
