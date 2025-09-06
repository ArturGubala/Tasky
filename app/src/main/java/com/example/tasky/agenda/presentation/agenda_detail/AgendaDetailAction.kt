package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaReminderInterval

interface AgendaDetailAction {
    data class OnAgendaReminderIntervalSelect(val reminder: AgendaReminderInterval): AgendaDetailAction
    data class OnTimeFromPick(val hour: Int, val minute: Int): AgendaDetailAction
    data class OnDateFromPick(val dateMillis: Long): AgendaDetailAction
}
