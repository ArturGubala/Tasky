@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.defaultAgendaItemIntervals
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class AgendaDetailState(
    val loadingInitialData: Boolean = false,
    val selectedAgendaReminderInterval: AgendaItemInterval = defaultAgendaItemIntervals().first(),
    val timestamp: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.HOURS),
    val selectedAttendeeStatus: AgendaItemAttendeesStatus = AgendaItemAttendeesStatus.ALL
) {
    val localTimestamp: ZonedDateTime
        get() = timestamp.withZoneSameInstant(ZoneId.systemDefault())
}
