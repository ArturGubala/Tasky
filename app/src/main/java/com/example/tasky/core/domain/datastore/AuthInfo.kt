package com.example.tasky.core.domain.datastore

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)
