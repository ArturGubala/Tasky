package com.example.tasky.auth.presentation.register

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data object OnLoginClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data class OnNameValueChanged(val name: String) : RegisterAction
    data class OnEmailValueChanged(val email: String) : RegisterAction
    data class OnPasswordValueChanged(val password: String) : RegisterAction
    data class OnFocusChanged(val field: FocusedField?, val hasFocus: Boolean) : RegisterAction
}
