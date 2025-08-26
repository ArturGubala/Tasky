package com.example.tasky.agenda.presentation

interface AgendaAction {
    data object OnLogoutClick: AgendaAction
    data object OnEventClick: AgendaAction
    data object OnTaskClick: AgendaAction
    data object OnReminderClick: AgendaAction
}
