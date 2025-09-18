package com.example.tasky.agenda.presentation.agenda_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextFieldPrimary
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AddAttendeeBottomSheetContent(
    onCLoseClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    attendeeEmail: String,
    onAttendeeEmailChange: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.add_visitor),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close bottom sheet icon",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                    onCLoseClick()
                },
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TaskyTextFieldPrimary(
                text = attendeeEmail,
                onValueChange = { onAttendeeEmailChange() }
            )
            TaskyPrimaryButton(
                onClick = { onAddClick() },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.error,
                enabled = false
            ) {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.add),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun AddAttendeeBottomSheetContentPreview() {
    TaskyTheme {
        TaskyBottomSheet(
            onDismiss = {},
        ) {
            AddAttendeeBottomSheetContent(
                onCLoseClick = {},
                onAddClick = {},
                attendeeEmail = "",
                onAttendeeEmailChange = {}
            )
        }
    }
}
