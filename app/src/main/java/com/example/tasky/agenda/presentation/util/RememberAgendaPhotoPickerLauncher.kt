package com.example.tasky.agenda.presentation.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState


@Composable
fun rememberAgendaPhotoPickerLauncher(
    onPhotoSelected: (Uri) -> Unit
): () -> Unit {
    val onSelected by rememberUpdatedState(onPhotoSelected)

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { uri ->
                    onSelected(uri)
            }
        }
    )

    return {
        singlePhotoLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

