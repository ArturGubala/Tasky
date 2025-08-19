package com.example.tasky.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibilityClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnRegisterClick: LoginAction
    data class OnEmailValueChanged(val email: String) : LoginAction
    data class OnPasswordValueChanged(val password: String) : LoginAction
    data class OnFocusChanged(val field: LoginFocusedField?, val hasFocus: Boolean) : LoginAction
}
