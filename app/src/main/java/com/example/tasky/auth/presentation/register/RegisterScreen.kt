package com.example.tasky.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyLink
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyPasswordTextField
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextField
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onLoginLinkClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            RegisterEvent.OnLoginClick -> {
                onLoginLinkClick()
            }
            is RegisterEvent.RegistrationFailure -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            RegisterEvent.RegistrationSuccess -> {
                keyboardController?.hide()
                Toast.makeText(
                    context,
                    R.string.registration_successful,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulRegistration()
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        onClearFocus = { focusManager.clearFocus() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    onClearFocus: (() -> Unit)? = null
) {
    val passwordState = rememberTextFieldState(initialText = state.password)

    LaunchedEffect(passwordState.text) {
        val newPassword = passwordState.text.toString()
        if (newPassword != state.password) {
            onAction(RegisterAction.OnPasswordValueChanged(newPassword))
        }
    }

    TaskyScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_your_account),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 36.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            onClearFocus?.invoke()
                        })
                    },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 28.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onClearFocus?.invoke()
                    })
                },
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TaskyTextField(
                    text = state.name,
                    onValueChange = { onAction(RegisterAction.OnNameValueChanged(it)) },
                    onFocusChanged = { hasFocus ->
                        onAction(RegisterAction.OnFocusChanged(FocusedField.NAME, hasFocus))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    hintText = stringResource(R.string.name),
                    isValid = state.isNameValid,
                    isFocused = state.focusedField == FocusedField.NAME,
                    errors = state.errors.filter { it.focusedField == FocusedField.NAME }
                )
                TaskyTextField(
                    text = state.email,
                    onValueChange = { onAction(RegisterAction.OnEmailValueChanged(it)) },
                    onFocusChanged = { hasFocus ->
                        onAction(RegisterAction.OnFocusChanged(FocusedField.EMAIL, hasFocus))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    hintText = stringResource(R.string.email_address),
                    isValid = state.isEmailValid,
                    isFocused = state.focusedField == FocusedField.EMAIL,
                    errors = state.errors.filter { it.focusedField == FocusedField.EMAIL }
                )
                TaskyPasswordTextField(
                    state = passwordState,
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePasswordVisibility = {
                        onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                    },
                    hintText = stringResource(R.string.password),
                    modifier = Modifier
                        .fillMaxWidth(),
                    onFocusChanged = { hasFocus ->
                        onAction(RegisterAction.OnFocusChanged(FocusedField.PASSWORD, hasFocus))
                    },
                    isValid = state.passwordValidationState.isValidPassword,
                    isFocused = state.focusedField == FocusedField.PASSWORD,
                    errors = state.errors.filter { it.focusedField == FocusedField.PASSWORD }
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TaskyPrimaryButton(
                    content = {
                        Text(
                            text = stringResource(R.string.get_started),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = {
                        onAction(RegisterAction.OnRegisterClick)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.canRegister
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                    TaskyLink(
                        onClick = {
                            onAction(RegisterAction.OnLoginClick)
                        },
                        content = {
                            Text(
                                text = stringResource(R.string.log_in),
                                color = MaterialTheme.colorScheme.extended.link,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
