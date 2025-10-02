package com.example.tasky.core.presentation.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TimeNeedleIndicator(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = MaterialTheme.colorScheme.primary)
        )
    }
}

@PreviewLightDark
@Composable
private fun TimeNeedleIndicatorPreview() {
    TaskyTheme {
        TimeNeedleIndicator(
        )
    }
}
