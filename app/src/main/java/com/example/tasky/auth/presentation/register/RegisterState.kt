package com.example.tasky.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.example.tasky.auth.domain.PasswordValidationState

class RegisterState (
    val name: String = "",
    val isNameValid: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false
)
