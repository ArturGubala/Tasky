package com.example.tasky.core.domain.util

//sealed interface DataError: Error {
//    enum class Network: DataError {
//        UNAUTHORIZED,
//        FORBIDDEN,
//        NOT_FOUND,
//        BAD_REQUEST,
//        CONFLICT,
//        TOO_MANY_REQUESTS,
//        NO_INTERNET,
//        SERVER_ERROR,
//        SERIALIZATION,
//        UNKNOWN
//    }
//
//    enum class Local: DataError {
//        DISK_FULL,
//        COMPRESSION_FAILURE,
//        IMAGE_TOO_LARGE
//    }
//}

sealed interface DataError: Error {
    sealed interface Network : DataError {
        data class BadRequest(val message: String? = null) : Network
        data object Unauthorized : Network
        data object Forbidden : Network
        data class NotFound(val message: String? = null) : Network
        data class Conflict(val message: String? = null) : Network
        data object TooManyRequests : Network
        data object ServerError : Network
        data object NoInternet : Network
        data object Serialization : Network
        data object Unknown : Network
    }

    sealed interface Local : DataError {
        data object DiskFull : Local
        data object CompressionFailure : Local
        data object ImageTooLarge : Local
    }
}
