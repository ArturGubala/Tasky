package com.example.tasky.core.presentation.designsystem.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.buttons.TaskySecondaryButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyModalDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isConfirmEnable: Boolean = true,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TaskyPrimaryButton(
                onClick = { onConfirm() },
                backgroundColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(.47f),
                enabled = isConfirmEnable
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    if (isConfirmEnable) {
                        Text(
                            text = stringResource(R.string.delete),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(size = 24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },
        dismissButton = {
            TaskySecondaryButton(
                text = stringResource(R.string.cancel),
                onClick = { onDismiss() },
                modifier = Modifier.fillMaxWidth(.47f),
            )
        },
        title = {
            Text(
                text = stringResource(R.string.are_you_sure),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.not_reversed_operation_warning),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        properties = DialogProperties(
            dismissOnClickOutside = true
        )
    )
}

@PreviewLightDark
@Composable
private fun TaskyModalDialogPreview() {
    TaskyTheme {
        TaskyModalDialog(
            onDismiss = {},
            onConfirm = {},
            isConfirmEnable = false
        )
    }
}
