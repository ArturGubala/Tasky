package com.example.tasky.auth.presentation.register

import com.example.tasky.core.presentation.ui.UiText

sealed interface RegisterEvent {

    data object RegistrationSuccess: RegisterEvent
    data class RegistrationFailure(val error: UiText): RegisterEvent
}
