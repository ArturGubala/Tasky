package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.designsystem.theme.headlineXSmall

@Composable
fun TaskyRadioButton() {
    var selectedOption by remember { mutableStateOf("All") }
    val options = listOf("All", "Going", "Not going")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(options) { option ->
            val isOptionSelected = selectedOption == option
            TaskyTextButton(
                onClick = { selectedOption = option },
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.extended.surfaceHigher
                )
            ) {
                Text(
                    text = option,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineXSmall
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyRadioButtonPreview() {
    TaskyTheme {
        TaskyTheme {
            TaskyRadioButton()
        }
    }
}
