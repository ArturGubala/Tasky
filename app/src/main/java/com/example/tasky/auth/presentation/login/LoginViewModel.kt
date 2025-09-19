package com.example.tasky.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.auth.domain.FocusedField
import com.example.tasky.auth.domain.UserDataValidator
import com.example.tasky.core.domain.ValidationItem
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()
    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> loginUser()
            LoginAction.OnRegisterClick -> onRegisterCLick()
            LoginAction.OnTogglePasswordVisibilityClick -> changePasswordVisibility()
            is LoginAction.OnEmailValueChanged -> {
                _state.update { it.copy(email = action.email) }
                validateFieldOnFocusLoss()
                _state.update { it.copy(canLogin = checkIfCanLogin()) }
            }
            is LoginAction.OnPasswordValueChanged -> {
                _state.update { it.copy(password = action.password) }
                validateFieldOnFocusLoss()
                _state.update { it.copy(canLogin = checkIfCanLogin()) }

            }
            is LoginAction.OnFocusChanged -> {
                handleFocusChange(action.field, action.hasFocus)
            }
        }
    }

    private fun changePasswordVisibility() {
        _state.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    private fun onRegisterCLick() {
        viewModelScope.launch {
            eventChannel.send(LoginEvent.OnRegisterClick)
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoggingIn = true) }
            val result = authRepository.login(
                email = _state.value.email.trim(),
                password = _state.value.password
            )
            _state.update { it.copy(isLoggingIn = false) }

            when(result) {
                is Result.Error -> {
                    eventChannel.send(LoginEvent.LoginFailure(
                        error = result.error.asUiText()
                    ))
                }
                is Result.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }

    private fun handleFocusChange(field: FocusedField?, hasFocus: Boolean) {
        if (!hasFocus && _state.value.focusedField != null) {
            validateFieldOnFocusLoss()
        }

        _state.update {
            it.copy(
                focusedField = if (hasFocus) field else null,
                canLogin = checkIfCanLogin()
            )
        }

    }

    private fun checkIfCanLogin(): Boolean{
        return _state.value.isEmailValid &&
                _state.value.isPasswordValid &&
                !_state.value.isLoggingIn
    }

    private fun validateFieldOnFocusLoss() {
        when (_state.value.focusedField) {
            LoginFocusedField.EMAIL -> {
                val isEmailValid = userDataValidator.validateEmail(_state.value.email)
                _state.update {
                    val updatedState = it.copy(isEmailValid = isEmailValid)
                    updatedState.copy(errors = getValidationItemsForFocusedField())
                }
            }
            LoginFocusedField.PASSWORD -> {
                val isPasswordValid = _state.value.password.isNotEmpty()
                _state.update {
                    val updatedState = it.copy(
                        isPasswordValid = isPasswordValid
                    )
                    updatedState.copy(errors = getValidationItemsForFocusedField())
                }
            }
            null -> {}
        }
    }

    private fun getValidationItemsForFocusedField(): List<ValidationItem> {
        return when (_state.value.focusedField) {
            LoginFocusedField.EMAIL -> getEmailValidationItems()
            LoginFocusedField.PASSWORD -> getPasswordValidationItems()
            null -> emptyList()
            else -> emptyList()
        }
    }

    private fun getEmailValidationItems(): List<ValidationItem> {
        return if (_state.value.email.isNotEmpty() && !_state.value.isEmailValid) {
            listOf(
                ValidationItem(
                    message = UiText.StringResource(R.string.must_be_a_valid_email),
                    isValid = _state.value.isEmailValid,
                    focusedField = LoginFocusedField.EMAIL
                )
            )
        } else emptyList()
    }

    private fun getPasswordValidationItems(): List<ValidationItem> {
        return if (_state.value.password.isEmpty() && !_state.value.isPasswordValid) {
            listOf(
                ValidationItem(
                    message = UiText.StringResource(R.string.field_can_not_be_empty),
                    isValid = _state.value.isEmailValid,
                    focusedField = LoginFocusedField.PASSWORD
                )
            )
        } else emptyList()
    }
}

sealed class LoginFocusedField : FocusedField {
    object EMAIL : LoginFocusedField()
    object PASSWORD : LoginFocusedField()
}
