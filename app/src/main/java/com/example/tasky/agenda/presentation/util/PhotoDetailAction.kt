package com.example.tasky.agenda.presentation.util

enum class PhotoDetailAction {
    DELETE,
    NONE
}

data class PhotoDetail(
    val photoId: String = "",
    val photoDetailAction: PhotoDetailAction = PhotoDetailAction.NONE
)
