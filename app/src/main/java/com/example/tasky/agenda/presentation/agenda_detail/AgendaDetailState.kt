@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tasky.agenda.presentation.util.AgendaReminderInterval
import com.example.tasky.agenda.presentation.util.AgendaDetailConfigProvider
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class AgendaDetailState(
    val loadingInitialData: Boolean = false,
    val selectedAgendaReminderInterval: AgendaReminderInterval = AgendaDetailConfigProvider
        .getDefaultReminderIntervals().first(),
    val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.HOURS)
) {
    val localTimestamp: ZonedDateTime
        get() = timestamp.withZoneSameInstant(ZoneId.systemDefault())
}
