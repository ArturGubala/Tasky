package com.example.tasky.agenda.presentation.agenda_edit

sealed interface AgendaEditTextEvent {
    data object OnReadyAfterCancelClick: AgendaEditTextEvent
    data class OnReadyAfterSaveClick(val text: String): AgendaEditTextEvent
}
