package com.example.tasky.auth.presentation.login

import com.example.tasky.auth.domain.ValidationItem

data class LoginState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val password: String = "",
    val isPasswordValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false,
    val errors: List<ValidationItem> = emptyList(),
)
