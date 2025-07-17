package com.example.tasky.core.presentation.designsystem.text_fields

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String? = null,
    hintColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    hasError: Boolean = false
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.extended.surfaceHigher,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (hasError) MaterialTheme.colorScheme.error
                        else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if(text.isBlank() && hintText != null) {
                        Text(
                            text = hintText,
                            color = if (isFocused) MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                            else hintColor,
                            style = textStyle
                        )
                    } else {
                        innerTextField()
                    }
                }

                if (text.isNotBlank() && !hasError) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Valid input",
                        tint = MaterialTheme.colorScheme.extended.success,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    )
}

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskyTextFieldPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp)
        ) {
            TaskyTextField(
                text = "",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address"
            )
            TaskyTextField(
                text = "propermail@gmail.com",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address"
            )
            TaskyTextField(
                text = "somemail@gmail.pl",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address",
                hasError = true
            )
        }
    }
}
