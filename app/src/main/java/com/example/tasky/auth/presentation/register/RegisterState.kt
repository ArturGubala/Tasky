package com.example.tasky.auth.presentation.register

import com.example.tasky.auth.domain.PasswordValidationState
import com.example.tasky.auth.domain.ValidationItem
import com.example.tasky.auth.domain.FocusedField

data class RegisterState (
    val name: String = "",
    val isNameValid: Boolean = false,
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val focusedField: FocusedField? = null,
    val errors: List<ValidationItem> = emptyList(),
    val canRegister: Boolean = false,
)
