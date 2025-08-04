package com.example.tasky.core.presentation.designsystem.text_fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.auth.presentation.register.FocusedField
import com.example.tasky.auth.presentation.register.ValidationItem
import com.example.tasky.core.presentation.designsystem.TaskyErrorText
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    hintText: String? = null,
    hintColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isValid: Boolean = false,
    isFocused: Boolean = false,
    errors: List<ValidationItem> = emptyList()
) {
    Column(
        modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = 150)),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.extended.surfaceHigher,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (!isValid && !isFocused && errors.isNotEmpty()) MaterialTheme.colorScheme.error
                    else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .onFocusChanged { focusState ->
                    onFocusChanged?.invoke(focusState.hasFocus)
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
                        if (text.isBlank() && hintText != null && isFocused) {
                            Row {
                                Text(
                                    text = hintText,
                                    color = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                                    style = textStyle
                                )
                                innerTextField()
                            }
                        } else if(text.isBlank() && hintText != null && !isFocused) {
                            Text(
                                text = hintText,
                                color = hintColor,
                                style = textStyle
                            )
                        } else {
                            innerTextField()
                        }
                    }

                    if (text.isNotBlank() && isValid) {
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
        AnimatedVisibility(
            visible = !isValid && !isFocused && errors.isNotEmpty(),
            enter = expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 250
                )
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = 250
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.extended.surfaceHigher,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                errors?.forEach { error ->
                    TaskyErrorText(
                        text = if (error.formatArgs.isNotEmpty()) {
                            stringResource(error.textResId, *error.formatArgs.toTypedArray())
                        } else {
                            stringResource(error.textResId)
                        },
                        isValid = error.isValid
                    )
                }
            }
        }
    }
}

@PreviewLightDark
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
                text = "",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address",
                isFocused = true
            )
            TaskyTextField(
                text = "propermail@gmail.com",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address",
                isValid = false,
                errors = listOf(
                    ValidationItem(
                        textResId = R.string.must_be_a_valid_email,
                        isValid = false,
                        focusedField = FocusedField.EMAIL
                    ),
                    ValidationItem(
                        textResId = R.string.must_be_a_valid_email,
                        isValid = true,
                        focusedField = FocusedField.EMAIL
                    )
                )
            )
            TaskyTextField(
                text = "somemail@gmail.pl",
                onValueChange = {},
                modifier = Modifier.width(328.dp),
                hintText = "Email address",
                isValid = true
            )
        }
    }
}
