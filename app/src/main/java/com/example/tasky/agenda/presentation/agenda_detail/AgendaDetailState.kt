@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tasky.agenda.domain.Attendee
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
    val selectedAttendeeStatus: AgendaItemAttendeesStatus = AgendaItemAttendeesStatus.ALL,
    // List for tests
    val sampleAttendees: List<Attendee> = listOf(
        Attendee("wade1@example.com", "Wade Warren", "1", "event1", true, "2024-01-15T09:30:00Z", true),
        Attendee("wade2@example.com", "Wade Warren", "2", "event1", true, "2024-01-15T09:30:00Z", false),
        Attendee("wade3@example.com", "Wade Warren", "3", "event1", true, "2024-01-15T09:30:00Z", false),
        Attendee("wade4@example.com", "Wade Warren", "4", "event1", false, "2024-01-15T09:30:00Z", false),
        Attendee("wade5@example.com", "Wade Warren", "5", "event1", false, "2024-01-15T09:30:00Z", false)
    )
) {
    val localTimestamp: ZonedDateTime
        get() = timestamp.withZoneSameInstant(ZoneId.systemDefault())
}
