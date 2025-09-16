package com.example.tasky.agenda.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.tasky.R
import com.example.tasky.agenda.domain.util.COMPRESS_QUALITY
import com.example.tasky.agenda.domain.util.MAX_SIZE_BYTES
import com.example.tasky.agenda.domain.util.OPEN_FILE_MODE
import com.example.tasky.core.presentation.ui.UiText
import java.io.ByteArrayOutputStream

@Composable
fun agendaPhotosPicker(
    onPhotoSelected: (Uri) -> Unit,
    maxSizeBytes: Int = MAX_SIZE_BYTES
): () -> Unit {
    val context = LocalContext.current

    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { photoUri ->
            photoUri?.let { uri ->
                if (uri.isImageSizeTooLargeToUpload(context, maxSizeBytes)) {
                    Toast.makeText(
                        context,
                        UiText.StringResource(
                            id = R.string.skip_x_photos,
                            args = arrayOf(1)
                        ).asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    onPhotoSelected(uri)
                }
            }
        }
    )

    return {
        singlePhotoLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}

fun Uri.isImageSizeTooLargeToUpload(
    context: Context,
    maxUploadSize: Int = MAX_SIZE_BYTES
): Boolean {
    this.getBytesRecompressed(context, maxUploadSize)?.size?.let { size ->
        return size > maxUploadSize
    }

    return true
}

fun Uri.getBytesRecompressed(
    context: Context,
    recompressThreshold: Int = MAX_SIZE_BYTES
): ByteArray?  {
    val imageSize = this.getSize(context) ?: return null
    var bytes = this.getBytes(context) ?: return null

    imageSize.also { size ->
        if (size > recompressThreshold) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, stream)
            bytes = stream.toByteArray()
        }
    }

    return bytes
}

fun Uri.getBytes(context: Context): ByteArray?  {
    return context.contentResolver
        .openInputStream(this)
        .use {
            it?.readBytes()
        }
}

fun Uri.getSize(context: Context): Long?  {
    return context.contentResolver.openFileDescriptor(this, OPEN_FILE_MODE)
        .use {
            it?.statSize
        }
}
