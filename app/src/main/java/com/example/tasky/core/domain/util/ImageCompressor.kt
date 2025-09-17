package com.example.tasky.core.domain.util

interface ImageCompressor {
    suspend fun compressFromUriString(uriString: String, maxBytes: Int): Result<ByteArray, DataError>
}
