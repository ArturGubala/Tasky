package com.example.tasky.auth.presentation.register

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.auth.domain.UserDataValidator
import com.example.tasky.auth.domain.ValidationRules
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> onLoginCLick()
            RegisterAction.OnRegisterClick -> registerUser()
            RegisterAction.OnTogglePasswordVisibilityClick -> changePasswordVisibility()
            is RegisterAction.OnNameValueChanged -> {
                _state.update { it.copy(name = action.name) }
                validateFieldOnFocusLoss(_state.value.focusedField!!)
                _state.update { it.copy(canRegister = checkIfCanRegister()) }
            }

            is RegisterAction.OnEmailValueChanged -> {
                _state.update { it.copy(email = action.email) }
                validateFieldOnFocusLoss(_state.value.focusedField!!)
                _state.update { it.copy(canRegister = checkIfCanRegister()) }
            }

            is RegisterAction.OnPasswordValueChanged -> {
                _state.update { it.copy(password = action.password) }
                validateFieldOnFocusLoss(_state.value.focusedField!!)
                _state.update { it.copy(canRegister = checkIfCanRegister()) }

            }

            is RegisterAction.OnFocusChanged -> {
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

    private fun onLoginCLick() {
        viewModelScope.launch {
            eventChannel.send(RegisterEvent.OnLoginClick)
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _state.update { it.copy(isRegistering = true) }
            val result = authRepository.register(
                fullName = _state.value.name.trim(),
                email = _state.value.email.trim(),
                password = _state.value.password
            )
            _state.update { it.copy(isRegistering = false) }

            when(result) {
                is Result.Error -> {
                    if(result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(RegisterEvent.RegistrationFailure(
                            error = UiText.StringResource(R.string.error_email_exists)
                        ))
                    } else {
                        eventChannel.send(RegisterEvent.RegistrationFailure(
                            error = result.error.asUiText()
                        ))
                    }
                }
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }

    private fun handleFocusChange(field: FocusedField?, hasFocus: Boolean) {
        if (!hasFocus && _state.value.focusedField != null) {
            validateFieldOnFocusLoss(_state.value.focusedField!!)
        }

        _state.update {
            it.copy(
                focusedField = if (hasFocus) field else null,
                canRegister = checkIfCanRegister()
            )
        }

    }

    private fun validateFieldOnFocusLoss(field: FocusedField) {
        when (field) {
            FocusedField.NAME -> {
                val isNameValid = userDataValidator.isValidName(_state.value.name)
                _state.update {
                    val updatedState = it.copy(isNameValid = isNameValid)
                    updatedState.copy(errors = getValidationItemsForFocusedField())
                }
            }
            FocusedField.EMAIL -> {
                val isEmailValid = userDataValidator.validateEmail(_state.value.email)
                _state.update {
                    val updatedState = it.copy(isEmailValid = isEmailValid)
                    updatedState.copy(errors = getValidationItemsForFocusedField())
                }
            }
            FocusedField.PASSWORD -> {
                val passwordValidationState =
                    userDataValidator.validatePassword(password = _state.value.password)
                _state.update {
                    val updatedState = it.copy(
                        passwordValidationState = passwordValidationState
                    )
                    updatedState.copy(errors = getValidationItemsForFocusedField())
                }
            }
        }
    }

    private fun checkIfCanRegister(): Boolean{
        return _state.value.isNameValid &&
                _state.value.isEmailValid &&
                _state.value.passwordValidationState.isValidPassword &&
                !_state.value.isRegistering
    }

    fun getValidationItemsForFocusedField(): List<ValidationItem> {
        return when (_state.value.focusedField) {
            FocusedField.NAME -> getNameValidationItems()
            FocusedField.EMAIL -> getEmailValidationItems()
            FocusedField.PASSWORD -> _state.value.passwordValidationState.getPasswordValidationItems()
            null -> emptyList()
        }
    }

    private fun getNameValidationItems(): List<ValidationItem> {
        return if (_state.value.name.isNotEmpty() && !_state.value.isNameValid) {
            listOf(
                ValidationItem(
                    textResId = R.string.must_be_a_valid_name,
                    isValid = _state.value.isNameValid,
                    formatArgs = listOf(ValidationRules.MIN_NAME_LENGTH, ValidationRules.MAX_NAME_LENGTH),
                    focusedField = FocusedField.NAME
                )
            )
        } else emptyList()
    }

    private fun getEmailValidationItems(): List<ValidationItem> {
        return if (_state.value.email.isNotEmpty() && !_state.value.isEmailValid) {
            listOf(
                ValidationItem(
                    textResId = R.string.must_be_a_valid_email,
                    isValid = _state.value.isEmailValid,
                    focusedField = FocusedField.EMAIL
                )
            )
        } else emptyList()
    }
}

enum class FocusedField {
    NAME, EMAIL, PASSWORD
}

data class ValidationItem(
    @StringRes val textResId: Int,
    val isValid: Boolean,
    val formatArgs: List<Any> = emptyList(),
    val focusedField: FocusedField
)
