package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaEditTextFieldType

interface AgendaDetailEvent {
    data class OnReadyAfterEditTextClick(
        val text: String,
        val fieldType: AgendaEditTextFieldType): AgendaDetailEvent
}
