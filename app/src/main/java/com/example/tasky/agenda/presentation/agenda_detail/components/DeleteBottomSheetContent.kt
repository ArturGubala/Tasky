package com.example.tasky.agenda.presentation.agenda_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.bottom_sheets.TaskyBottomSheet
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.buttons.TaskySecondaryButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun DeleteBottomSheetContent(
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
//            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 24.dp),
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.not_reversed_operation_warning),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskySecondaryButton(
                text = stringResource(R.string.cancel),
                onClick = { onCancel() },
                modifier = Modifier.weight(.5f),
            )
            TaskyPrimaryButton(
                onClick = { onDelete() },
                modifier = Modifier.weight(.5f),
                backgroundColor = MaterialTheme.colorScheme.error,
                enabled = true
            ) {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DeleteBottomSheetContentPreview() {
    TaskyTheme {
        TaskyBottomSheet(
            onDismiss = {},
        ) {
            DeleteBottomSheetContent(
                onDelete = {},
                onCancel = {},
                title = "Delete task?"
            )
        }
    }
}
