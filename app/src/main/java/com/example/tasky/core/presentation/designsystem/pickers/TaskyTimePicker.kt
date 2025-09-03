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
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@SuppressLint("DefaultLocale")
@Composable
fun TaskyTimePicker(
    time: String,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean = false,
    onTimeSelected: (String) -> Unit = {},
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(time) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.extended.surfaceHigher)
            .clickable(enabled = !isReadOnly) {
                showTimePicker = true
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Select time",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    if (showTimePicker && !isReadOnly) {
        val timePickerState = rememberTimePickerState()

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TaskyTextButton(
                    onClick = {
                        selectedTime = String.format(
                            "%02d:%02d",
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        onTimeSelected(selectedTime)
                        showTimePicker = false
                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Confirm"
                    )
                }

            },
            title = {
                Text(
                    text = "Chose time"
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
                        text = "Cancel"
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
        TaskyTimePicker(
            time = "10:15",
            isReadOnly = false,
            onTimeSelected = {},
            modifier = Modifier
                .width(120.dp)
        )
    }
}
