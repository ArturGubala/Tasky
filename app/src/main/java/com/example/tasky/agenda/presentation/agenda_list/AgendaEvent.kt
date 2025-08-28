package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.core.presentation.ui.UiText

interface AgendaEvent {
    data class LogoutFailure(val error: UiText) : AgendaEvent
    data object LogoutSuccessful : AgendaEvent
}
