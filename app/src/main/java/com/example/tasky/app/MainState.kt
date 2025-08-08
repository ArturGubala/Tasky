package com.example.tasky.app

import com.example.tasky.core.domain.AuthState

data class MainState(
    val isCheckingAuth: Boolean = false,
    val authState: AuthState = AuthState.NOT_AUTHENTICATED
)
