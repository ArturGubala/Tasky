package com.example.tasky.agenda.presentation.agenda_detail

import android.net.Uri
import androidx.core.net.toUri
import com.example.tasky.agenda.domain.model.Photo

data class PhotoUi(
    val id: String,
    val uri: Uri
)

fun Photo.toUi(): PhotoUi = PhotoUi(
    id = id,
    uri = uri.toUri()
)
