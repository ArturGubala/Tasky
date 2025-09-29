package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetAttendeeResponseDto(
    val email: String,
    val fullName: String,
    val userId: String,
)
