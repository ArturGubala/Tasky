package com.example.tasky.core.domain.util

sealed interface DataError: Error {
    enum class Network: DataError {
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        BAD_REQUEST,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        COMPRESSION_FAILURE,
        IMAGE_TOO_LARGE
    }
}
