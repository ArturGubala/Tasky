package com.example.tasky.core.presentation.designsystem.text_fields

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyTextFieldSecondary(
    textFieldState: TextFieldState,
    textStyle: TextStyle,
    cursorBrush: Brush,
    isSingleLine: Boolean,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        state = textFieldState,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        textStyle = textStyle,
        lineLimits = if (isSingleLine) {
            TextFieldLineLimits.SingleLine
        } else {
            TextFieldLineLimits.MultiLine()
        },
        cursorBrush = cursorBrush
    )
}

@PreviewLightDark
@Composable
fun TaskyTextFieldSecondaryPreview() {
    TaskyTheme {
        val textFieldState = rememberTextFieldState("TEST")

        TaskyTextFieldSecondary(
            textFieldState = textFieldState,
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            isSingleLine = true
        )
    }
}
