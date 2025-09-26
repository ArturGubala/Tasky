package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailView

interface AgendaAction {
    data object OnLogoutClick: AgendaAction
    data object OnFabButtonClick: AgendaAction
    data object OnProfileButtonClick: AgendaAction
    data class OnFabMenuOptionClick(
        val agendaKind: AgendaKind,
        val agendaDetailView: AgendaDetailView,
        val agendaId: String = "",
    ) : AgendaAction
}
