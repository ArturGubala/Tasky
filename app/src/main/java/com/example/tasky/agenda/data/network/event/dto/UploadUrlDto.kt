package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadUrlDto(
    val photoKey: String,
    val uploadKey: String,
    val url: String,
)
