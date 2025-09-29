package com.example.tasky.agenda.data.network.event.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpsertEventResponseDto(
    val event: EventDto,
    val uploadUrls: List<UploadUrlDto>,
)
