@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.presentation.agenda_list.AgendaEvent
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.core.domain.util.ConnectivityObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AgendaDetailViewModel(
    private val agendaId: String,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaDetailState())
    val state = _state
        .onStart {
            if (_state.value.loadingInitialData) {
                return@onStart
            }

            _state.update { it.copy(loadingInitialData = true) }
            if (agendaId.isNotEmpty()) {
                // TODO: Get agenda details from db by provided id
            }
            _state.update { it.copy(loadingInitialData = false) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaDetailState(),
        )
    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AgendaDetailAction) {
        when(action) {
           is AgendaDetailAction.OnAgendaItemIntervalSelect -> onAgendaItemIntervalSelect(
               reminder = action.reminder
           )
           is AgendaDetailAction.OnTimeFromPick -> {
               val updatedTimestamp = updateUtcTime(currentTimestamp = _state.value.timestamp,
                   hour = action.hour, minute = action.minute)
               _state.update {
                   it.copy(
                       timestamp = updatedTimestamp
                   )
               }
           }
            is AgendaDetailAction.OnDateFromPick -> {
                val updatedTimestamp = updateUtcDate(currentTimestamp = _state.value.timestamp,
                    dateMillis = action.dateMillis)
                _state.update {
                    it.copy(
                        timestamp = updatedTimestamp
                    )
                }
            }
        }
    }

    private fun onAgendaItemIntervalSelect(reminder: AgendaItemInterval) {
        _state.update {
            it.copy(selectedAgendaReminderInterval = reminder)
        }
    }

    private fun updateUtcTime(currentTimestamp: ZonedDateTime, hour: Int, minute: Int): ZonedDateTime {
        val localTime = LocalTime.of(hour, minute)
        val localDate = currentTimestamp.toLocalDate()
        val localDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault())
        val utcDateTime = localDateTime.withZoneSameInstant(ZoneId.of("UTC"))
        return utcDateTime
    }

    private fun updateUtcDate(currentTimestamp: ZonedDateTime, dateMillis: Long): ZonedDateTime {
        val localTime = LocalTime.of(currentTimestamp.hour, currentTimestamp.minute)
        val localDate = epochMillisToLocalDate(dateMillis)
        val localDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"))
        return localDateTime
    }

    private fun epochMillisToLocalDate(epochMillis: Long): LocalDate {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}
