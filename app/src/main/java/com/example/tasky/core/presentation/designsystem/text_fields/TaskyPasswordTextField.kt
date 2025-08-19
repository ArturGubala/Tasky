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
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.tasky.auth.domain.ValidationItem
import com.example.tasky.core.presentation.designsystem.TaskyErrorText
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyPasswordTextField(
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    hintText: String?,
    modifier: Modifier = Modifier,
    onFocusChanged: ((Boolean) -> Unit)? = null,
    hintColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurface
    ),
    isValid: Boolean = false,
    isFocused: Boolean = false,
    errors: List<ValidationItem> = emptyList()
) {
    Column(
        modifier = modifier.animateContentSize(animationSpec = tween(durationMillis = 150)),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        BasicSecureTextField(
            state = state,
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
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorator = TextFieldDecorator { innerTextField ->

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {

                        if (state.text.isBlank() && hintText != null && isFocused) {
                            Row {
                                Text(
                                    text = hintText,
                                    color = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                                    style = textStyle
                                )
                                innerTextField()
                            }
                        } else if (state.text.isBlank() && hintText != null && !isFocused) {
                            Text(
                                text = hintText,
                                color = hintColor,
                                style = textStyle
                            )
                        } else {
                            innerTextField()
                        }
                    }

                    IconButton(onClick = onTogglePasswordVisibility, modifier = Modifier.size(20.dp)) {
                        Icon(
                            imageVector = if (!isPasswordVisible) {
                                Icons.Default.VisibilityOff
                            } else Icons.Default.Visibility,
                            contentDescription = if(isPasswordVisible) {
                                stringResource(id = R.string.show_password)
                            } else {
                                stringResource(id = R.string.hide_password)
                            },
                            tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                        )
                    }
                }
            },
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else TextObfuscationMode.Hidden,
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
                    .padding(start = 5.dp, top = 8.dp , end = 20.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                errors.forEach { error ->
                    TaskyErrorText(
                        text = error.message.asString(),
                        isValid = error.isValid
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PasswordTextFieldPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp)
        ) {
            TaskyPasswordTextField(
                state = rememberTextFieldState(),
                isPasswordVisible = false,
                onTogglePasswordVisibility = {},
                hintText = "Password",
                modifier = Modifier.width(328.dp)
            )
            TaskyPasswordTextField(
                state = rememberTextFieldState(),
                isPasswordVisible = false,
                onTogglePasswordVisibility = {},
                hintText = "Password",
                modifier = Modifier.width(328.dp),
                isFocused = true
            )
            TaskyPasswordTextField(
                state = rememberTextFieldState(
                    initialText = "1234567890"
                ),
                isPasswordVisible = false,
                onTogglePasswordVisibility = {},
                hintText = "Email address",
                modifier = Modifier.width(328.dp)
            )
            TaskyPasswordTextField(
                state = rememberTextFieldState(
                    initialText = "1234567890"
                ),
                isPasswordVisible = true,
                onTogglePasswordVisibility = {},
                hintText = "Email address",
                modifier = Modifier.width(328.dp)
            )
        }
    }
}
