package com.example.tasky.core.presentation.designsystem.labels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.icons.TaskyCircle
import com.example.tasky.core.presentation.designsystem.icons.TaskySquare
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyLabel(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    labelLeadingIcon: @Composable (() -> Unit) = {},
    placeholder: String? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        labelLeadingIcon.invoke()
        if (placeholder == null) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                style = textStyle
            )
        } else {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                style = textStyle
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyLabelPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TaskyLabel(
                text = "TASK",
                textStyle = MaterialTheme.typography.labelMedium,
                modifier = Modifier,
                labelLeadingIcon = {
                    TaskySquare(
                        size = 20.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                    )
                }
            )
            TaskyLabel(
                text = "Project X",
                textStyle = MaterialTheme.typography.headlineLarge,
                modifier = Modifier,
                labelLeadingIcon = {
                    TaskyCircle(
                        size = 20.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                    )
                }
            )
            TaskyLabel(
                text = "Project X",
                textStyle = MaterialTheme.typography.headlineLarge,
                modifier = Modifier,
                labelLeadingIcon = {
                    TaskyCircle(
                        size = 20.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                    )
                },
                placeholder = "Placeholder"
            )
        }
    }
}
