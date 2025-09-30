@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.tasky.agenda.domain.model.EventAttendee
import com.example.tasky.agenda.domain.model.LookupAttendee
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.presentation.util.AgendaDetailBottomSheetType
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.defaultAgendaItemIntervals
import com.example.tasky.core.domain.ValidationItem
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.ExperimentalTime

data class AgendaDetailState(
    val loadingInitialData: Boolean = false,
    val isOnline: Boolean = false,
    val isDeleting: Boolean = false,
    val title: String = "Project X",
    val description: String? = "Weekly plan\n Role distribution",
    val selectedAgendaReminderInterval: AgendaItemInterval = defaultAgendaItemIntervals().first(),
    val fromTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")).truncatedTo(ChronoUnit.HOURS),
    val selectedAttendeeStatus: AgendaItemAttendeesStatus = AgendaItemAttendeesStatus.ALL,
    val details: AgendaItemDetails? = AgendaItemDetails.Event(),
    val agendaDetailBottomSheetType: AgendaDetailBottomSheetType = AgendaDetailBottomSheetType.NONE,
)

sealed interface AgendaItemDetails {
    data class Event(
        val toTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"))
            .truncatedTo(ChronoUnit.HOURS).plusHours(1),
        val lookupAttendees: List<LookupAttendee> = listOf(),
        val eventAttendees: List<EventAttendee> = listOf(),
        val photos: List<Photo> = emptyList(),
        val newPhotosIds: List<String> = emptyList(),
        val deletedPhotosIds: List<String> = emptyList(),
        val isImageLoading: Boolean = false,
        val attendeeEmail: String = "",
        val isAttendeeEmailValid: Boolean = false,
        val isAttendeeEmailFocused: Boolean = false,
        val errors: List<ValidationItem> = emptyList(),
        val isAttendeeOperationInProgress: Boolean = false,

        ) : AgendaItemDetails

    data class Task(
        val isDone: Boolean = false
    ): AgendaItemDetails

    data object Reminder: AgendaItemDetails
}

fun AgendaDetailState.detailsAsEvent(): AgendaItemDetails.Event? {
    return details as? AgendaItemDetails.Event
}

fun AgendaDetailState.detailsAsTask(): AgendaItemDetails.Task? {
    return details as? AgendaItemDetails.Task
}

fun AgendaDetailState.isReminder(): Boolean {
    return details is AgendaItemDetails.Reminder
}
