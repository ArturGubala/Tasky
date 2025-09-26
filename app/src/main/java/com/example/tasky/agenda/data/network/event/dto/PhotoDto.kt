package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val key: String,
    val url: String,
)
