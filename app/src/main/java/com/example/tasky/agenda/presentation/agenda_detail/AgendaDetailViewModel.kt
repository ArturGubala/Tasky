@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.presentation.util.AgendaDetailBottomSheetType
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.auth.presentation.register.RegisterFocusedField
import com.example.tasky.core.domain.PatternValidator
import com.example.tasky.core.domain.ValidationItem
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.ImageCompressor
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val default: CoroutineDispatcher = Dispatchers.Default,
    private val patternValidator: PatternValidator
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
            observeConnectivity()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaDetailState(),
        )
    private val eventChannel = Channel<AgendaDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .onEach { isConnected ->
                _state.update {
                    it.copy(
                        isOnline = isConnected
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AgendaDetailAction) {
        when(action) {
           is AgendaDetailAction.OnAgendaItemIntervalSelect -> onAgendaItemIntervalSelect(
               reminder = action.reminder
           )
           is AgendaDetailAction.OnTimeFromPick -> {
               val updatedTimestamp = updateUtcTime(currentTimestamp = _state.value.fromTime,
                   hour = action.hour, minutes = action.minute)

               _state.value.detailsAsEvent()?.toTime?.let { currentToTimestamp ->
                   if (updatedTimestamp >= currentToTimestamp) {
                       viewModelScope.launch(default) {
                           eventChannel.send(AgendaDetailEvent.InvalidDatePicked(
                               error = UiText.StringResource(R.string.date_from_before_date_to)
                           ))
                       }
                       return
                   }
               }

               _state.update {
                   it.copy(
                       fromTime = updatedTimestamp
                   )
               }
           }
            is AgendaDetailAction.OnDateFromPick -> {
                val updatedTimestamp = updateUtcDate(currentTimestamp = _state.value.fromTime,
                    dateMillis = action.dateMillis)

                _state.value.detailsAsEvent()?.toTime?.let { currentToTimestamp ->
                    if (updatedTimestamp >= currentToTimestamp) {
                        viewModelScope.launch(default) {
                            eventChannel.send(AgendaDetailEvent.InvalidDatePicked(
                                error = UiText.StringResource(R.string.date_from_before_date_to)
                            ))
                        }
                        return
                    }
                }

                _state.update {
                    it.copy(
                        fromTime = updatedTimestamp
                    )
                }
            }
            is AgendaDetailAction.OnTimeToPick -> {
                _state.value.detailsAsEvent()?.toTime?.let { currentTimestamp ->
                    val updatedTimestamp = updateUtcTime(currentTimestamp = currentTimestamp,
                        hour = action.hour, minutes = action.minute)

                    if (updatedTimestamp <= _state.value.fromTime) {
                        viewModelScope.launch(default) {
                            eventChannel.send(AgendaDetailEvent.InvalidDatePicked(
                                error = UiText.StringResource(R.string.date_to_after_date_from)
                            ))
                        }
                        return
                    }

                    updateDetails<AgendaItemDetails.Event> { event ->
                        event.copy(
                            toTime = updatedTimestamp
                        )
                    }
                }
            }
            is AgendaDetailAction.OnDateToPick -> {
                _state.value.detailsAsEvent()?.toTime?.let { currentTimestamp ->
                    val updatedTimestamp = updateUtcDate(currentTimestamp = currentTimestamp,
                        dateMillis = action.dateMillis)

                    if (updatedTimestamp <= _state.value.fromTime) {
                        viewModelScope.launch(default) {
                            eventChannel.send(AgendaDetailEvent.InvalidDatePicked(
                                error = UiText.StringResource(R.string.date_to_after_date_from)
                            ))
                        }
                        return
                    }

                    updateDetails<AgendaItemDetails.Event> { event ->
                        event.copy(
                            toTime = updatedTimestamp
                        )
                    }
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
                viewModelScope.launch(default) {
                    updateDetails<AgendaItemDetails.Event> { event ->
                        event.copy(isImageLoading = true)
                    }

                    when (val result = compressor.compressFromUriString(
                       uriString = action.uriString, maxBytes = action.maxBytes)) {
                        is Result.Success -> {
                            val bytes = result.data
                            val photo = Photo(
                                id = UUID.randomUUID().toString(),
                                uri = action.uriString,
                                compressedBytes = bytes
                            )
                            updateDetails<AgendaItemDetails.Event> { event ->
                                event.copy(
                                    photos = event.photos + photo
                                )
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

                    updateDetails<AgendaItemDetails.Event> { event ->
                        event.copy(isImageLoading = false)
                    }
                }
            }
            is AgendaDetailAction.OnPhotoDelete -> {
                updateDetails<AgendaItemDetails.Event> { event ->
                    event.copy(
                        photos = event.photos.filterNot { photo ->
                            photo.id == action.photoId
                        }
                    )
                }
            }
            AgendaDetailAction.OnAddAttendeeClick -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.ADD_ATTENDEE) }
            }
            AgendaDetailAction.OnDeleteAgendaItemClick -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.DELETE_AGENDA_ITEM) }
            }
            AgendaDetailAction.OnDismissBottomSheet -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.NONE) }
            }
            is AgendaDetailAction.OnAttendeeEmailValueChanged -> {
                updateDetails<AgendaItemDetails.Event> { event ->
                    event.copy(
                        attendeeEmail = action.email
                    )
                }
            }
            is AgendaDetailAction.OnAttendeeEmailFieldFocusChanged -> {
                val email = _state.value.detailsAsEvent()?.attendeeEmail ?: ""
                val isEmailValid = if (!action.hasFocus) {
                    validateEmail(email)
                } else false
                val errors = if (email.isNotEmpty() && !isEmailValid) {
                    listOf(
                        ValidationItem(
                            message = UiText.StringResource(R.string.must_be_a_valid_email),
                            isValid = isEmailValid,
                            focusedField = RegisterFocusedField.EMAIL
                        )
                    )
                } else emptyList()

                updateDetails<AgendaItemDetails.Event> { event ->
                    event.copy(
                        isAttendeeEmailFocused = action.hasFocus,
                        isAttendeeEmailValid = isEmailValid,
                        errors = errors
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

    private fun updateUtcTime(currentTimestamp: ZonedDateTime, hour: Int, minutes: Int): ZonedDateTime {
        return currentTimestamp
            .withZoneSameInstant(ZoneId.systemDefault())
            .withHour(hour)
            .withMinute(minutes)
            .withZoneSameInstant(ZoneId.of("UTC"))
    }

    private fun updateUtcDate(currentTimestamp: ZonedDateTime, dateMillis: Long): ZonedDateTime {
        return currentTimestamp
            .withZoneSameInstant(ZoneId.systemDefault())
            .with(epochMillisToLocalDate(dateMillis))
            .withZoneSameInstant(ZoneId.of("UTC"))
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

    private inline fun <reified T : AgendaItemDetails> updateDetails(transform: (T) -> T) {
        _state.update { state ->
            (state.details as? T)?.let { details ->
                state.copy(details = transform(details))
            } ?: state
        }
    }

    fun validateEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }
}
