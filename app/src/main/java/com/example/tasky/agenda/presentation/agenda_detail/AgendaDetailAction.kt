package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaItemInterval

interface AgendaDetailAction {
    data class OnAgendaItemIntervalSelect(val reminder: AgendaItemInterval): AgendaDetailAction
    data class OnTimeFromPick(val hour: Int, val minute: Int): AgendaDetailAction
    data class OnDateFromPick(val dateMillis: Long): AgendaDetailAction
}
