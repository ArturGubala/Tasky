@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

data class AgendaDetailState(
    val loadingInitialData: Boolean = false,
    val timeFromState: TimePickerState =
        TimePickerState( initialHour = 0, initialMinute = 0, is24Hour = true),
    val dateFromState: DatePickerState =
        DatePickerState(locale = Locale.getDefault(), initialSelectedDateMillis = ZonedDateTime.now(
            ZoneId.of("UTC")).toEpochSecond()),
    val timeToState: TimePickerState =
        TimePickerState( initialHour = 0, initialMinute = 0, is24Hour = true),
    val dateToState: DatePickerState =
        DatePickerState(locale = Locale.getDefault(), initialSelectedDateMillis = ZonedDateTime.now(
            ZoneId.of("UTC")).toEpochSecond()),
)
