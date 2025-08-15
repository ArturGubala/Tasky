@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyLink
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyPasswordTextField
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextField
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import com.example.tasky.core.presentation.util.DeviceConfiguration
import com.example.tasky.core.presentation.util.clearFocusOnTapOutside
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

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    onClearFocus: (() -> Unit)? = null
) {
    val passwordState = rememberTextFieldState(initialText = state.password)

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val Header = rememberRegisterHeader(R.string.create_your_account)

    LaunchedEffect(passwordState.text) {
        val newPassword = passwordState.text.toString()
        if (newPassword != state.password) {
            onAction(RegisterAction.OnPasswordValueChanged(newPassword))
        }
    }

    TaskyScaffold { padding ->

        when(deviceConfiguration) {
            DeviceConfiguration.MOBILE_PORTRAIT -> {
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    Header(
                        Modifier
                            .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 36.dp)
                    )
                    TaskyContentBox(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        RegisterFormSection(
                            state = state,
                            passwordState = passwordState,
                            onAction = onAction,
                            onClearFocus = onClearFocus
                        )
                    }
                }
            }
            DeviceConfiguration.MOBILE_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .padding(padding)
                        .padding(start = 24.dp, top = 18.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Header(
                        Modifier
                            .padding(end = 24.dp)
                            .widthIn(max = 220.dp)
                            .fillMaxHeight()
                    )
                    TaskyContentBox(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            RegisterFormSection(
                                state = state,
                                passwordState = passwordState,
                                onAction = onAction,
                                onClearFocus = onClearFocus
                            )
                        }
                    }
                }
            }
            DeviceConfiguration.TABLET_PORTRAIT,
            DeviceConfiguration.TABLET_LANDSCAPE,
            DeviceConfiguration.DESKTOP -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 156.dp)
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                ) {
                    Header(Modifier.padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 36.dp))
                    TaskyContentBox(
                        contentAlignment = Alignment.TopCenter
                    ) {
                        RegisterFormSection(
                            state = state,
                            passwordState = passwordState,
                            onAction = onAction,
                            onClearFocus = onClearFocus
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegisterFormSection(
    state: RegisterState,
    passwordState: TextFieldState,
    onAction: (RegisterAction) -> Unit,
    onClearFocus: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 28.dp)
            .clearFocusOnTapOutside(onClear = { onClearFocus?.invoke() }),
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

@Composable
private fun rememberRegisterHeader(textRes: Int) = remember(textRes) {
    movableContentOf<Modifier> { modifier ->
        Box(
            modifier = modifier,
            contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.3f)
        ) {
            Text(
                text = stringResource(textRes),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                maxLines = 2,
                softWrap = true
            )
        }
    }
}

@PreviewLightDark
@PreviewScreenSizes
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}
