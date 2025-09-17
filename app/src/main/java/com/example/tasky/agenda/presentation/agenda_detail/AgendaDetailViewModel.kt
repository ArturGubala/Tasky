@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.ImageCompressor
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

class AgendaDetailViewModel(
    private val agendaId: String,
    private val agendaItemType: AgendaItemType,
    private val connectivityObserver: ConnectivityObserver,
    private val compressor: ImageCompressor,
    private val io: CoroutineDispatcher = Dispatchers.IO
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
            when(agendaItemType) {
                AgendaItemType.TASK -> _state.update { it.copy(details = AgendaItemDetails.Task()) }
                AgendaItemType.EVENT -> _state.update { it.copy(details = AgendaItemDetails.Event()) }
                AgendaItemType.REMINDER -> _state.update { it.copy(details = AgendaItemDetails.Reminder) }
            }
            _state.update { it.copy(loadingInitialData = false) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaDetailState(),
        )
    private val eventChannel = Channel<AgendaDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AgendaDetailAction) {
        when(action) {
           is AgendaDetailAction.OnAgendaItemIntervalSelect -> onAgendaItemIntervalSelect(
               reminder = action.reminder
           )
           is AgendaDetailAction.OnTimeFromPick -> {
               val updatedTimestamp = updateUtcTime(currentTimestamp = _state.value.fromTime,
                   hour = action.hour, minutes = action.minute)
               _state.update {
                   it.copy(
                       fromTime = updatedTimestamp
                   )
               }
           }
            is AgendaDetailAction.OnDateFromPick -> {
                val updatedTimestamp = updateUtcDate(currentTimestamp = _state.value.fromTime,
                    dateMillis = action.dateMillis)
                _state.update {
                    it.copy(
                        fromTime = updatedTimestamp
                    )
                }
            }
            is AgendaDetailAction.OnAttendeeStatusClick -> changeAttendeeStatus(action.status)
            is AgendaDetailAction.OnTitleChange -> {
                _state.update {
                    it.copy(
                        title = action.title
                    )
                }
            }
            is AgendaDetailAction.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = action.description
                    )
                }
            }
            is AgendaDetailAction.OnPhotoSelected -> {
                viewModelScope.launch(io) {
                _state.update { it.copy(imageLoading = true) }
                    when (val result = compressor.compressFromUriString(
                       uriString = action.uriString, maxBytes = action.maxBytes)) {
                        is Result.Success -> {
                            val bytes = result.data
                            val photo = Photo(
                                id = UUID.randomUUID().toString(),
                                uri = action.uriString,
                                compressedBytes = bytes
                            )

                            _state.update { it ->
                                it.detailsAsEvent()?.let { eventDetails ->
                                    val updatedDetails = eventDetails.copy(
                                        photos = eventDetails.photos + photo
                                    )
                                    it.copy(details = updatedDetails)
                                } ?: it
                            }
                        }
                        is Result.Error -> {
                            when (result.error as DataError.Local) {
                                DataError.Local.COMPRESSION_FAILURE -> {
                                    eventChannel.send(AgendaDetailEvent.ImageCompressFailure(
                                        error = result.error.asUiText()
                                    ))
                                }
                                DataError.Local.IMAGE_TOO_LARGE -> {
                                    eventChannel.send(AgendaDetailEvent.ImageTooLarge(
                                            error = result.error.asUiText()
                                    ))
                                }
                                else -> Unit
                            }
                        }
                    }
                _state.update { it.copy(imageLoading = false) }
                }
            }
            is AgendaDetailAction.OnPhotoDelete -> {
                _state.update { it ->
                    it.detailsAsEvent()?.let { eventDetails ->
                        val updatedDetails = eventDetails.copy(
                            photos = eventDetails.photos.filterNot { photo ->
                                photo.id == action.photoId
                            }
                        )
                        it.copy(details = updatedDetails)
                    } ?: it
                }
            }
        }
    }

    private fun onAgendaItemIntervalSelect(reminder: AgendaItemInterval) {
        _state.update {
            it.copy(selectedAgendaReminderInterval = reminder)
        }
    }

    private fun updateUtcTime(currentTimestamp: ZonedDateTime, hour: Int, minutes: Int): ZonedDateTime {
        val localTime = LocalTime.of(hour, minutes)
        val localDate = currentTimestamp.toLocalDate()
        val localDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault())
        return localDateTime.withZoneSameInstant(ZoneId.of("UTC"))
    }

    private fun updateUtcDate(currentTimestamp: ZonedDateTime, dateMillis: Long): ZonedDateTime {
        val localTime = LocalTime.of(currentTimestamp.hour, currentTimestamp.minute)
        val localDate = epochMillisToLocalDate(dateMillis)
        return ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"))
    }

    private fun epochMillisToLocalDate(epochMillis: Long): LocalDate {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun changeAttendeeStatus(status: AgendaItemAttendeesStatus) {
        _state.update {
            it.copy(selectedAttendeeStatus = status)
        }
    }
}
