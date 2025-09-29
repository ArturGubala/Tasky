package com.example.tasky.core.domain.util

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    val status: Int,
    val reason: List<String>,
)
