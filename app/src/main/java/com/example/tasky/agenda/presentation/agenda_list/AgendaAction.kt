package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import java.time.ZonedDateTime

interface AgendaAction {
    data object OnLogoutClick : AgendaAction
    data object OnFabButtonClick : AgendaAction
    data object OnProfileButtonClick : AgendaAction
    data class OnFabMenuOptionClick(
        val agendaKind: AgendaKind,
        val agendaDetailView: AgendaDetailView,
        val agendaId: String = "",
    ) : AgendaAction

    data object OnDismissModalDialog : AgendaAction
    data class OnDeleteMenuOptionClick(val id: String) : AgendaAction
    data object OnConfirmDeleteClick : AgendaAction
    data class OnAgendaItemMenuClick(val id: String) : AgendaAction
    data class OnDismissAgendaItemMenu(val id: String) : AgendaAction
    data class OnCompleteTaskClick(val id: String, val isDone: Boolean) : AgendaAction
    data class OnDateSelect(val date: ZonedDateTime) : AgendaAction
    data object OnDatePickerDismiss : AgendaAction
    data object OnCalendarIconClick : AgendaAction
    data object OnMonthClick : AgendaAction
}
