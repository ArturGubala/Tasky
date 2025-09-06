@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tasky.agenda.presentation.util.AgendaReminderInterval
import com.example.tasky.agenda.presentation.util.AgendaTypeConfigProvider
import java.time.ZoneId
import java.time.ZonedDateTime

data class AgendaDetailState(
    val loadingInitialData: Boolean = false,
    val selectedAgendaReminderInterval: AgendaReminderInterval = AgendaTypeConfigProvider
        .getDefaultReminderIntervals().first(),
    val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"))
) {
    val localTimestamp: ZonedDateTime
        get() = timestamp.withZoneSameInstant(ZoneId.systemDefault())
}
