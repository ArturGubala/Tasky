package com.example.tasky.core.presentation.ui

import com.example.tasky.R
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.presentation.ui.UiText.DynamicString
import com.example.tasky.core.presentation.ui.UiText.StringResource

fun DataError.asUiText(): UiText {
    return when(this) {
        is DataError.Local.DiskFull -> StringResource(R.string.error_disk_full)
        is DataError.Local.CompressionFailure -> StringResource(R.string.error_compress_image)
        is DataError.Local.ImageTooLarge -> StringResource(R.string.too_large_photo_error)

        is DataError.Network.BadRequest -> toUiText(R.string.error_bad_request)
        is DataError.Network.NotFound -> toUiText(R.string.error_not_found)
        is DataError.Network.Conflict -> toUiText(R.string.error_conflict)

        is DataError.Network.Unauthorized -> StringResource(R.string.error_unauthorized)
        is DataError.Network.Forbidden -> StringResource(R.string.error_forbidden)
        is DataError.Network.TooManyRequests -> StringResource(R.string.error_too_many_requests)
        is DataError.Network.NoInternet -> StringResource(R.string.error_no_internet)
        is DataError.Network.ServerError -> StringResource(R.string.error_server_error)
        is DataError.Network.Serialization -> StringResource(R.string.error_serialization)
        is DataError.Network.Unknown -> StringResource(R.string.error_unknown)
    }
}

private fun DataError.Network.toUiText(fallbackRes: Int): UiText {
    val message = when (this) {
        is DataError.Network.BadRequest -> this.message
        is DataError.Network.NotFound -> this.message
        is DataError.Network.Conflict -> this.message
        else -> null
    }
    return message?.let { DynamicString(it) } ?: StringResource(fallbackRes)
}
