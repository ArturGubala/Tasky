package com.example.tasky.agenda.data.network.event.dto

data class GetAttendeeResponseDto(
    val email: String,
    val fullName: String,
    val userId: String,
)
