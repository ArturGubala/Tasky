package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.agenda.domain.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.core.presentation.ui.UiText

interface AgendaEvent {
    data class LogoutFailure(val error: UiText) : AgendaEvent
    data object LogoutSuccessful : AgendaEvent
    data class OnFabMenuOptionClick(
        val agendaItemType: AgendaItemType,
        val agendaDetailView: AgendaDetailView,
        val agendaId: String = ""
    ) : AgendaEvent
}
