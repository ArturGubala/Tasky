package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfig

data class AgendaDetailState(
    val agendaDetailView: AgendaDetailView,
    val agendaItemType: AgendaItemType,
    val agendaDetailConfig: AgendaTypeConfig,
    val agendaId: String? = null
)
