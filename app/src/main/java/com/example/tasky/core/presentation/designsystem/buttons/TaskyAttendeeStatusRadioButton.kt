package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.designsystem.theme.headlineXSmall

@Composable
fun TaskyAttendeeStatusRadioButton(
    options: List<AgendaItemAttendeesStatus>,
    onOptionSelect: (AgendaItemAttendeesStatus) -> Unit,
    modifier: Modifier = Modifier,
    selectedOption: AgendaItemAttendeesStatus = AgendaItemAttendeesStatus.ALL,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween
) {
    LazyRow(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
    ) {
        items(options) { option ->
            val isOptionSelected = selectedOption == option
            TaskyTextButton(
                onClick = { onOptionSelect(option) },
                modifier = Modifier
                    .background(
                        color = if (isOptionSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.extended.surfaceHigher
                        },
                        shape = RoundedCornerShape(100.dp)
                    )
                    .requiredWidth(107.dp)
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.getDisplayName(LocalContext.current),
                    color = if (isOptionSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.headlineXSmall
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyAttendeeStatusRadioButtonPreview() {
    TaskyTheme {
        var selectedOption by remember { mutableStateOf(AgendaItemAttendeesStatus.ALL) }
        TaskyAttendeeStatusRadioButton(
            options = AgendaItemAttendeesStatus.entries,
            onOptionSelect = { selectedOption = it },
            selectedOption = selectedOption
        )
    }
}
