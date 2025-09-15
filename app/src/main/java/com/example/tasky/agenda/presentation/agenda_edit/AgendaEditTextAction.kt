package com.example.tasky.agenda.presentation.agenda_edit

sealed interface AgendaEditTextAction {
    data object OnCancelClick: AgendaEditTextAction
    data class OnSaveClick(val text: String): AgendaEditTextAction
}
