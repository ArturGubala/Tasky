package com.example.tasky.agenda.domain.model

data class Photo(
    val id: String,
    val uri: String,
    val compressedBytes: ByteArray? = null
)
