package com.example.tasky.auth.presentation.login

import com.example.tasky.core.presentation.ui.UiText

sealed interface LoginEvent {
    data class LoginFailure(val error: UiText): LoginEvent
    data object LoginSuccess: LoginEvent
    data object OnRegisterClick: LoginEvent
}
