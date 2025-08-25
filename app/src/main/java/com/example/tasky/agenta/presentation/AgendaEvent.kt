package com.example.tasky.agenta.presentation

import com.example.tasky.core.presentation.ui.UiText

interface AgendaEvent {
    data class LogoutFailure(val error: UiText) : AgendaEvent
    data object LogoutSuccessful : AgendaEvent
}
