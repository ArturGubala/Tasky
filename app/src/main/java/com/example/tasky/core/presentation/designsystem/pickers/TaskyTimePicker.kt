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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
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

@SuppressLint("DefaultLocale")
@Composable
fun TaskyTimePicker(
    selectedTime: String,
    onValueChange: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.extended.surfaceHigher)
            .clickable(enabled = !isReadOnly) {
                showTimePicker = true
            }
            .padding(start = 12.dp, top = 4.dp, end = 4.dp, bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
            )

            if (!isReadOnly) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select time",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showTimePicker && !isReadOnly) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TaskyTextButton(
                    onClick = {
                        showTimePicker = false
                        onValueChange(timePickerState.hour, timePickerState.minute)
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.confirm)
                    )
                }

            },
            title = {
                Text(
                    text = stringResource(R.string.chose_time)
                )
            },
            containerColor = MaterialTheme.colorScheme.extended.surfaceHigher,
            dismissButton = {
                TaskyTextButton(
                    onClick = { showTimePicker = false },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            }
        ) {
            TimePicker(
                state = timePickerState
            )
        }
    }
}

@PreviewLightDark
@Composable
fun TaskyTimePickerPreview() {
    TaskyTheme {
//        TaskyTimePicker(
//            selectedTime = "01:23",
//            timePickerState = TimePickerState(0, 0, true),
//            isReadOnly = false,
//            modifier = Modifier
//                .width(120.dp)
//        )
    }
}
