package com.example.tasky.core.presentation.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyErrorText(
    text: String,
    modifier: Modifier = Modifier,
    isValid: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isValid) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Success icon",
                tint = MaterialTheme.colorScheme.extended.success
            )
            ErrorText(text = text, MaterialTheme.colorScheme.extended.success)
        } else {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Success icon",
                tint = MaterialTheme.colorScheme.error
            )
            ErrorText(text = text, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun ErrorText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp)
    )
}

@PreviewLightDark
@Composable
private fun TaskyErrorTextPreview() {
    TaskyTheme {
        Column {
            TaskyErrorText(text = "Name must be between 4 and 50 characters long.")
            TaskyErrorText(
                text = "Name must be between 4 and 50 characters long.",
                isValid = true)
        }
    }
}
