package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.onSurfaceVariantOpacity70

@Composable
fun PrimaryButton(
    content: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariantOpacity70,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier
    ) {
        content.invoke()
    }
}

@PreviewLightDark
@Composable
private fun PrimaryButtonPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp)
        ) {
            PrimaryButton(
                content = {
                    Text(
                        text = "Get started",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                onClick = {}
            )
            PrimaryButton(
                content = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size = 24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                onClick = {},
                enabled = false
            )
        }
    }
}
