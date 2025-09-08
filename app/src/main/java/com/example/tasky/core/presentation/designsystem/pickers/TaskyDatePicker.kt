@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.core.presentation.designsystem.pickers

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import java.time.ZonedDateTime

@SuppressLint("DefaultLocale")
@Composable
fun TaskyDatePicker(
    selectedDate: String,
    onValueChange: (dateMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.extended.surfaceHigher)
            .clickable(enabled = !isReadOnly) {
                showDatePicker = true
            }
            .padding(start = 12.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )

            if (!isReadOnly) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.select_date),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showDatePicker && !isReadOnly) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TaskyTextButton(
                    onClick = {
                        showDatePicker = false
                        onValueChange(datePickerState.selectedDateMillis ?: ZonedDateTime.now().toEpochSecond())
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.confirm)
                    )
                }
            },
            dismissButton = {
                TaskyTextButton(
                    onClick = { showDatePicker = false },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.choose_date),
                        modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                    )
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun TaskyDatePickerPreview() {
    TaskyTheme {
        TaskyDatePicker(
            selectedDate = "Jul 21, 2022",
            onValueChange = {},
            isReadOnly = false,
            modifier = Modifier
                .width(150.dp)
        )
    }
}
