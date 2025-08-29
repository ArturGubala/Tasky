package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.presentation.util.AgendaMode
import com.example.tasky.agenda.presentation.util.AgendaType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfig

data class AgendaDetailState(
    val agendaMode: AgendaMode,
    val agendaType: AgendaType,
    val agendaDetailConfig: AgendaTypeConfig,
    val agendaId: String? = null
)
