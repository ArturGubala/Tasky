@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.domain.ValidationItem
import com.example.tasky.core.presentation.designsystem.bottom_sheets.TaskyBottomSheet
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextFieldPrimary
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.util.clearFocusOnTapOutside

@Composable
fun AddAttendeeBottomSheetContent(
    onCloseClick: () -> Unit,
    onAddClick: () -> Unit,
    attendeeEmail: String,
    isAttendeeEmailValid: Boolean,
    isAttendeeEmailFieldFocused: Boolean,
    onAttendeeEmailChange: (String) -> Unit,
    onAttendeeEmailFieldFocusChange:((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    errors: List<ValidationItem> = emptyList()
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 24.dp)
            .clearFocusOnTapOutside(onClear = { focusManager.clearFocus() }),
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
                        onCloseClick()
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
                onValueChange = { onAttendeeEmailChange(it) },
                onFocusChanged = { hasFocus -> onAttendeeEmailFieldFocusChange?.invoke(hasFocus) },
                hintText = stringResource(R.string.email_address),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                isValid = isAttendeeEmailValid,
                isFocused = isAttendeeEmailFieldFocused,
                singleLine = true,
                errors = errors,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
            TaskyPrimaryButton(
                onClick = { onAddClick() },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.primary,
                enabled = isAttendeeEmailValid
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
        val sheetState = rememberModalBottomSheetState()
        TaskyBottomSheet(
            onDismiss = {},
            sheetState = sheetState,
            content = {
                AddAttendeeBottomSheetContent(
                    onCloseClick = {},
                    onAddClick = {},
                    attendeeEmail = "",
                    onAttendeeEmailChange = {},
                    isAttendeeEmailValid = false,
                    isAttendeeEmailFieldFocused = false,
                    onAttendeeEmailFieldFocusChange = {}
                )
            },
        )
    }
}
