package com.example.tasky.core.presentation.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clearFocusOnTapOutside(onClear: () -> Unit) = pointerInput(Unit) {
    detectTapGestures(onTap = { onClear() })
}
