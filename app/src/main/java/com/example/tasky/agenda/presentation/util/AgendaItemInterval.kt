package com.example.tasky.agenda.presentation.util

import com.example.tasky.R
import com.example.tasky.core.presentation.ui.UiText
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

sealed interface AgendaItemInterval {
    data object TenMinutesFromNow : AgendaItemInterval
    data object ThirtyMinutesFromNow : AgendaItemInterval
    data object OneHourFromNow : AgendaItemInterval
    data object SixHoursFromNow : AgendaItemInterval
    data object OneDayFromNow : AgendaItemInterval
}

fun AgendaItemInterval.toUiText(): UiText = when (this) {
    AgendaItemInterval.TenMinutesFromNow    -> UiText.StringResource(R.string.reminder_10_min)
    AgendaItemInterval.ThirtyMinutesFromNow -> UiText.StringResource(R.string.reminder_30_min)
    AgendaItemInterval.OneHourFromNow       -> UiText.StringResource(R.string.reminder_1_hour)
    AgendaItemInterval.SixHoursFromNow      -> UiText.StringResource(R.string.reminder_6_hours)
    AgendaItemInterval.OneDayFromNow        -> UiText.StringResource(R.string.reminder_1_day)
}

fun ZonedDateTime.apply(interval: AgendaItemInterval): ZonedDateTime = when (interval) {
    AgendaItemInterval.TenMinutesFromNow -> minusMinutes(10)
    AgendaItemInterval.ThirtyMinutesFromNow -> minusMinutes(30)
    AgendaItemInterval.OneHourFromNow -> minusHours(1)
    AgendaItemInterval.SixHoursFromNow -> minusHours(6)
    AgendaItemInterval.OneDayFromNow -> minusDays(1)
}

fun defaultAgendaItemIntervals(): List<AgendaItemInterval> = listOf(
    AgendaItemInterval.TenMinutesFromNow,
    AgendaItemInterval.ThirtyMinutesFromNow,
    AgendaItemInterval.OneHourFromNow,
    AgendaItemInterval.SixHoursFromNow,
    AgendaItemInterval.OneDayFromNow
)

fun Duration.toAgendaItemInterval(): AgendaItemInterval = when (this) {
    10.minutes -> AgendaItemInterval.TenMinutesFromNow
    30.minutes -> AgendaItemInterval.ThirtyMinutesFromNow
    1.hours -> AgendaItemInterval.OneHourFromNow
    6.hours -> AgendaItemInterval.SixHoursFromNow
    1.days -> AgendaItemInterval.OneDayFromNow
    else -> AgendaItemInterval.TenMinutesFromNow
}
