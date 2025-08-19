package com.example.tasky.core.domain.datastore

import kotlinx.serialization.Serializable
// TODO: Data classes in domain should be serialized?
@Serializable
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userName: String,
    val userId: String
)
