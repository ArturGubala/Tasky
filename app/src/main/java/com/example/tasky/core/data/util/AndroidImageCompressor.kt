package com.example.tasky.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.tasky.core.domain.util.ImageCompressor
import com.example.tasky.core.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.isActive
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale
import com.example.tasky.core.domain.util.DataError


class AndroidImageCompressor(
    private val context: Context,
    private val default: CoroutineDispatcher = Dispatchers.Default
) : ImageCompressor {

    override suspend fun compressFromUriString(
        uriString: String,
        maxBytes: Int,
    ): Result<ByteArray, DataError> = withContext(default) {
        try {
            val uri = uriString.toUri()
            val compressedBytes = compressImageToFitSize(uri, maxBytes)
            Result.Success(compressedBytes)
        } catch (_: ImageTooLargeException) {
            Result.Error(DataError.Local.IMAGE_TOO_LARGE)
        } catch (_: Exception) {
            Result.Error(DataError.Local.COMPRESSION_FAILURE)
        }
    }

    private suspend fun compressImageToFitSize(uri: Uri, maxBytes: Int): ByteArray {
        coroutineContext.ensureActive()

        val originalBytes = getBytes(uri)?: error("Cannot read image")

        if (originalBytes.size <= maxBytes) return originalBytes

        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
            ?: error("Cannot decode bitmap")

        var quality = INITIAL_COMPRESS_QUALITY
        var compressedBytes: ByteArray

        do {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            compressedBytes = stream.toByteArray()
            quality -= QUALITY_DECREMENT
        } while (coroutineContext.isActive && compressedBytes.size > maxBytes && quality > MIN_COMPRESS_QUALITY)

        if (compressedBytes.size > maxBytes) {
            coroutineContext.ensureActive()
            val resizedBitmap = resizeBitmapToFitSize(bitmap, maxBytes)
            val stream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, MIN_COMPRESS_QUALITY, stream)
            compressedBytes = stream.toByteArray()

            if (compressedBytes.size > maxBytes) {
                throw ImageTooLargeException()
            }
        }

        coroutineContext.ensureActive()
        return compressedBytes
    }

    private fun resizeBitmapToFitSize(bitmap: Bitmap, maxBytes: Int): Bitmap {
        val currentBytes = bitmap.byteCount
        val scaleFactor = kotlin.math.sqrt(maxBytes.toDouble() / currentBytes.toDouble())

        val newWidth = (bitmap.width * scaleFactor).toInt()
        val newHeight = (bitmap.height * scaleFactor).toInt()

        return bitmap.scale(newWidth, newHeight)
    }

    private fun getBytes(uri: Uri): ByteArray? {
        return try {
            context.contentResolver
                .openInputStream(uri)
                ?.use { inputStream ->
                    inputStream.readBytes()
                }
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        const val MAX_SIZE_BYTES = 1_000_000
        private const val INITIAL_COMPRESS_QUALITY = 90
        private const val MIN_COMPRESS_QUALITY = 50
        private const val QUALITY_DECREMENT = 10
    }
}

private class ImageTooLargeException(message: String = "") : Exception(message)
