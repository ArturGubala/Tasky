package com.example.tasky.core.presentation.designsystem.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskySquare(
    size: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@PreviewLightDark
@Composable
private fun TaskySquarePreview() {
    TaskyTheme {
        TaskySquare(
            size = 20.dp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
        )
    }
}
