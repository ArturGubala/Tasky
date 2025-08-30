package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType

interface AgendaAction {
    data object OnLogoutClick: AgendaAction
    data object OnFabButtonClick: AgendaAction
    data object OnProfileButtonClick: AgendaAction
    data class OnFabMenuOptionClick(
        val agendaItemType: AgendaItemType,
        val agendaDetailView: AgendaDetailView,
        val agendaId: String = ""
    ) : AgendaAction
}
