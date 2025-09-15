package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval

interface AgendaDetailAction {
    data class OnAgendaItemIntervalSelect(val reminder: AgendaItemInterval): AgendaDetailAction
    data class OnTimeFromPick(val hour: Int, val minute: Int): AgendaDetailAction
    data class OnDateFromPick(val dateMillis: Long): AgendaDetailAction
    data class OnAttendeeStatusClick(val status: AgendaItemAttendeesStatus): AgendaDetailAction
    data class OnEditTitleClick(val title: String): AgendaDetailAction
    data class OnEditDescriptionClick(val description: String): AgendaDetailAction
    data class OnTitleChange(val title: String): AgendaDetailAction
    data class OnDescriptionChange(val description: String): AgendaDetailAction
}
