package com.example.tasky.agenda.data.network.task.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val time: String,
    val remindAt: String,
    val updatedAt: String?,
    val isDone: Boolean,
)
