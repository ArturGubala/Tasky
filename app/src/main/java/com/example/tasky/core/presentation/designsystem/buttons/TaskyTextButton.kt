package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyTextButton(
    onClick: () -> Unit,
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
    ) {
        content.invoke()
    }
}

@PreviewLightDark
@Composable
private fun TaskyTextButtonPreview() {
    TaskyTheme {
        TaskyTextButton(
            onClick = {},
            content = {
                Text(
                    text = "LOG IN",
                    color = MaterialTheme.colorScheme.extended.link,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        )
    }
}
