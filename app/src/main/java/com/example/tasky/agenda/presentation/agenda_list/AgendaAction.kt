package com.example.tasky.agenda.presentation.agenda_list

interface AgendaAction {
    data object OnLogoutClick: AgendaAction
    data object OnEventClick: AgendaAction
    data object OnTaskClick: AgendaAction
    data object OnReminderClick: AgendaAction
    data object OnFabButtonClick: AgendaAction
    data object OnProfileButtonClick: AgendaAction
}
