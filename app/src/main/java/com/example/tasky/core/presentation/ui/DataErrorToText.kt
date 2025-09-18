package com.example.tasky.core.presentation.ui

import com.example.tasky.R
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.presentation.ui.UiText.*

fun DataError.asUiText(): UiText {
    return when(this) {
        DataError.Local.DISK_FULL -> StringResource(
            R.string.error_disk_full
        )
        DataError.Local.COMPRESSION_FAILURE -> StringResource(
            R.string.error_compress_image
        )
        DataError.Local.IMAGE_TOO_LARGE -> StringResource(
            R.string.too_large_photo_error
        )
        DataError.Network.TOO_MANY_REQUESTS -> StringResource(
            R.string.error_too_many_requests
        )
        DataError.Network.NO_INTERNET -> StringResource(
            R.string.error_no_internet
        )
        DataError.Network.SERVER_ERROR -> StringResource(
            R.string.error_server_error
        )
        DataError.Network.SERIALIZATION -> StringResource(
            R.string.error_serialization
        )
        DataError.Network.UNAUTHORIZED -> StringResource(
            R.string.error_unauthorized
        )
        DataError.Network.FORBIDDEN -> StringResource(
            R.string.error_forbidden
        )
        DataError.Network.NOT_FOUND -> StringResource(
            R.string.error_not_found
        )
        DataError.Network.BAD_REQUEST -> StringResource(
            R.string.error_bad_request
        )
        DataError.Network.CONFLICT -> StringResource(
            R.string.error_conflict
        )
        DataError.Network.UNKNOWN -> StringResource(
            R.string.error_unknown
        )
        else -> StringResource(R.string.error_unknown)
    }
}
