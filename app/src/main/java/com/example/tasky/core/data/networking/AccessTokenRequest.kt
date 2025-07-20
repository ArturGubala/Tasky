package com.example.tasky.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest (
    val refreshToken: String
)
