@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda.domain.data.EventRepository
import com.example.tasky.agenda.domain.data.ReminderRepository
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.model.Event
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.agenda.domain.model.Task
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailBottomSheetType
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.apply
import com.example.tasky.agenda.presentation.util.toAgendaItemInterval
import com.example.tasky.agenda.presentation.util.toKotlinInstant
import com.example.tasky.agenda.presentation.util.updateUtcDate
import com.example.tasky.agenda.presentation.util.updateUtcTime
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.domain.PatternValidator
import com.example.tasky.core.domain.ValidationItem
import com.example.tasky.core.domain.data.EventLocalDataSource
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.data.TaskLocalDataSource
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.ImageCompressor
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.onError
import com.example.tasky.core.domain.util.onSuccess
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

class AgendaDetailViewModel(
    private val agendaId: String,
    private val agendaKind: AgendaKind,
    private val connectivityObserver: ConnectivityObserver,
    private val compressor: ImageCompressor,
    private val default: CoroutineDispatcher = Dispatchers.Default,
    private val patternValidator: PatternValidator,
    private val taskRepository: TaskRepository,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val reminderRepository: ReminderRepository,
    private val reminderLocalDataSource: ReminderLocalDataSource,
    private val eventRepository: EventRepository,
    private val eventLocalDataSource: EventLocalDataSource,
    private val sessionStorage: SessionStorage,
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaDetailState())
    val state = _state
        .onStart {
            if (_state.value.loadingInitialData) {
                return@onStart
            }

            _state.update { it.copy(loadingInitialData = true) }
            when (agendaKind) {
                AgendaKind.TASK -> {
                    _state.update { it.copy(details = AgendaItemDetails.Task()) }
                    if (agendaId.isNotEmpty()) {
                        getTask(taskId = agendaId)
                    }
                }
                AgendaKind.EVENT -> {
                    _state.update { it.copy(details = AgendaItemDetails.Event()) }
                    if (agendaId.isNotEmpty()) {
                        getEvent(eventId = agendaId)
                    }
                }
                AgendaKind.REMINDER -> {
                    _state.update { it.copy(details = AgendaItemDetails.Reminder) }
                    if (agendaId.isNotEmpty()) {
                        getReminder(reminderId = agendaId)
                    }
                }
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

    private fun getTask(taskId: String) {
        viewModelScope.launch {
            taskLocalDataSource.getTask(id = taskId).firstOrNull()?.let { task ->
                val duration = task.time - task.remindAt
                _state.update {
                    it.copy(
                        title = task.title,
                        description = task.description,
                        fromTime = ZonedDateTime.ofInstant(
                            task.time.toJavaInstant(),
                            ZoneId.of("UTC")
                        ),
                        selectedAgendaReminderInterval = duration.toAgendaItemInterval()
                    )
                }
            }
        }
    }

    private fun getReminder(reminderId: String) {
        viewModelScope.launch {
            reminderLocalDataSource.getReminder(id = reminderId).firstOrNull()?.let { reminder ->
                val duration = reminder.time - reminder.remindAt
                _state.update {
                    it.copy(
                        title = reminder.title,
                        description = reminder.description,
                        fromTime = ZonedDateTime.ofInstant(
                            reminder.time.toJavaInstant(),
                            ZoneId.of("UTC")
                        ),
                        selectedAgendaReminderInterval = duration.toAgendaItemInterval()
                    )
                }
            }
        }
    }

    private fun getEvent(eventId: String) {
        viewModelScope.launch {
            val userId = sessionStorage.get()?.userId ?: ""
            eventLocalDataSource.getEvent(id = eventId, userId = userId).firstOrNull()
                ?.let { event ->
                val duration = event.timeFrom - event.remindAt
                _state.update {
                    it.copy(
                        title = event.title,
                        description = event.description,
                        fromTime = ZonedDateTime.ofInstant(
                            event.timeFrom.toJavaInstant(),
                            ZoneId.of("UTC")
                        ),
                        selectedAgendaReminderInterval = duration.toAgendaItemInterval(),
                    )
                }
                updateDetails<AgendaItemDetails.Event> {
                    it.copy(
                        toTime = ZonedDateTime.ofInstant(
                            event.timeTo.toJavaInstant(),
                            ZoneId.of("UTC")
                        ),
                        lookupAttendees = event.lookupAttendees,
                        eventAttendees = event.eventAttendees,
                        photos = event.photos,
                        isUserEventCreator = event.eventAttendees
                            .first { attendee -> attendee.userId == userId }.isCreator
                    )
                }
            }
        }
    }

    fun onAction(action: AgendaDetailAction) {
        when(action) {
            is AgendaDetailAction.OnAgendaItemIntervalSelect ->
                onAgendaItemIntervalSelect(reminder = action.reminder)

            is AgendaDetailAction.OnTimeFromPick -> {
               val updatedTimestamp = updateUtcTime(currentTimestamp = _state.value.fromTime,
                   hour = action.hour, minutes = action.minute)

                _state.value.detailsAsEvent().toTime.let { currentToTimestamp ->
                   if (updatedTimestamp >= currentToTimestamp) {
                       updateDetails<AgendaItemDetails.Event> { event ->
                           event.copy(
                               toTime = updatedTimestamp.plusHours(1)
                           )
                       }
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

                _state.value.detailsAsEvent().toTime.let { currentToTimestamp ->
                    if (updatedTimestamp >= currentToTimestamp) {
                        updateDetails<AgendaItemDetails.Event> { event ->
                            event.copy(
                                toTime = updatedTimestamp.plusHours(1)
                            )
                        }
                    }
                }

                _state.update {
                    it.copy(
                        fromTime = updatedTimestamp
                    )
                }
            }
            is AgendaDetailAction.OnTimeToPick -> {
                _state.value.detailsAsEvent().toTime.let { currentTimestamp ->
                    val updatedTimestamp = updateUtcTime(currentTimestamp = currentTimestamp,
                        hour = action.hour, minutes = action.minute)

                    if (updatedTimestamp <= _state.value.fromTime) {
                        _state.update { it.copy(fromTime = updatedTimestamp.minusHours(1)) }
                    }

                    updateDetails<AgendaItemDetails.Event> { event ->
                        event.copy(
                            toTime = updatedTimestamp
                        )
                    }
                }
            }
            is AgendaDetailAction.OnDateToPick -> {
                _state.value.detailsAsEvent().toTime.let { currentTimestamp ->
                    val updatedTimestamp = updateUtcDate(currentTimestamp = currentTimestamp,
                        dateMillis = action.dateMillis)

                    if (updatedTimestamp <= _state.value.fromTime) {
                        _state.update { it.copy(fromTime = updatedTimestamp.minusHours(1)) }
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
                                    photos = event.photos + photo,
                                    newPhotosIds = event.newPhotosIds + photo.id
                                )
                            }
                        }
                        is Result.Error -> {
                            when (result.error as DataError.Local) {
                                DataError.Local.CompressionFailure -> {
                                    eventChannel.send(AgendaDetailEvent.ImageCompressFailure(
                                        error = result.error.asUiText()
                                    ))
                                }

                                DataError.Local.ImageTooLarge -> {
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
                        },
                        deletedPhotosIds = event.deletedPhotosIds + action.photoId
                    )
                }
            }
            AgendaDetailAction.OnAddAttendeeClick -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.ADD_ATTENDEE) }
            }
            is AgendaDetailAction.OnDeleteAttendeeClick -> deleteAttendee(action.userId)
            AgendaDetailAction.OnDeleteAgendaItemClick -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.DELETE_AGENDA_ITEM) }
            }
            AgendaDetailAction.OnDismissBottomSheet -> {
                _state.update { it.copy(agendaDetailBottomSheetType = AgendaDetailBottomSheetType.NONE) }
                updateDetails<AgendaItemDetails.Event> { event ->
                    event.copy(
                        attendeeEmail = ""
                    )
                }
            }
            is AgendaDetailAction.OnAttendeeEmailValueChanged -> {
                updateDetails<AgendaItemDetails.Event> { event ->
                    event.copy(
                        attendeeEmail = action.email
                    )
                }
            }
            is AgendaDetailAction.OnAttendeeEmailFieldFocusChanged -> {
                val email = _state.value.detailsAsEvent().attendeeEmail
                val isEmailValid = if (!action.hasFocus) {
                    validateEmail(email)
                } else false
                val errors = if (email.isNotEmpty() && !isEmailValid) {
                    listOf(
                        ValidationItem(
                            message = UiText.StringResource(R.string.must_be_a_valid_email),
                            isValid = isEmailValid,
                            focusedField = null
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
            is AgendaDetailAction.OnSaveClick -> {
                when (action.agendaKind) {
                    AgendaKind.TASK -> saveTask()
                    AgendaKind.EVENT -> saveEvent()
                    AgendaKind.REMINDER -> saveReminder()
                }
            }
            is AgendaDetailAction.OnDeleteOnBottomSheetClick -> {
                when (action.agendaKind) {
                    AgendaKind.TASK -> deleteTask(action.id)
                    AgendaKind.EVENT -> deleteEvent(action.id)
                    AgendaKind.REMINDER -> deleteReminder(action.id)
                }
            }
            is AgendaDetailAction.OnAddOnBottomSheetClick -> addAttendee(action.email)
        }
    }

    private fun onAgendaItemIntervalSelect(reminder: AgendaItemInterval) {
        _state.update {
            it.copy(selectedAgendaReminderInterval = reminder)
        }
    }

    private fun updateUtcTime(currentTimestamp: ZonedDateTime, hour: Int, minutes: Int): ZonedDateTime {
        return currentTimestamp.updateUtcTime(hour, minutes)
    }

    private fun updateUtcDate(currentTimestamp: ZonedDateTime, dateMillis: Long): ZonedDateTime {
        return currentTimestamp.updateUtcDate(dateMillis)
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

    private fun validateEmail(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    private fun saveTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTimestamp = ZonedDateTime.now(ZoneId.of("UTC"))
            val task = Task(
                id = agendaId.ifEmpty { UUID.randomUUID().toString() },
                title = _state.value.title,
                description = _state.value.description,
                time = _state.value.fromTime.toKotlinInstant(),
                remindAt = _state.value.fromTime
                    .apply(_state.value.selectedAgendaReminderInterval)
                    .toKotlinInstant(),
                updatedAt = currentTimestamp.toKotlinInstant(),
                isDone = _state.value.detailsAsTask().isDone
            )

            val syncOperation =
                if (agendaId.isNotEmpty()) SyncOperation.UPDATE else SyncOperation.CREATE
            taskRepository.upsertTask(task, syncOperation)
                .onSuccess { responseData ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveSuccessful(
                            message = UiText.StringResource(R.string.save_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveError(
                            error = error.asUiText()
                        )
                    )
                }
        }
    }

    private fun saveReminder() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTimestamp = ZonedDateTime.now(ZoneId.of("UTC"))
            val reminder = Reminder(
                id = agendaId.ifEmpty { UUID.randomUUID().toString() },
                title = _state.value.title,
                description = _state.value.description,
                time = _state.value.fromTime.toKotlinInstant(),
                remindAt = _state.value.fromTime
                    .apply(_state.value.selectedAgendaReminderInterval)
                    .toKotlinInstant(),
                updatedAt = currentTimestamp.toKotlinInstant()
            )

            val syncOperation =
                if (agendaId.isNotEmpty()) SyncOperation.UPDATE else SyncOperation.CREATE
            reminderRepository.upsertReminder(reminder, syncOperation)
                .onSuccess { responseData ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveSuccessful(
                            message = UiText.StringResource(R.string.save_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveError(
                            error = error.asUiText()
                        )
                    )
                }
        }
    }

    private fun saveEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentTimestamp = ZonedDateTime.now(ZoneId.of("UTC"))
            val event = Event(
                id = agendaId.ifEmpty { UUID.randomUUID().toString() },
                title = _state.value.title,
                description = _state.value.description,
                timeFrom = _state.value.fromTime.toKotlinInstant(),
                timeTo = _state.value.detailsAsEvent().toTime.toKotlinInstant(),
                remindAt = _state.value.fromTime
                    .apply(_state.value.selectedAgendaReminderInterval)
                    .toKotlinInstant(),
                updatedAt = currentTimestamp.toKotlinInstant(),
                hostId = sessionStorage.get()?.userId ?: "",
                isUserEventCreator = true,
                lookupAttendees = _state.value.detailsAsEvent().lookupAttendees,
                eventAttendees = _state.value.detailsAsEvent().eventAttendees,
                photos = _state.value.detailsAsEvent().photos,
                newPhotosIds = _state.value.detailsAsEvent().newPhotosIds,
                deletedPhotosIds = _state.value.detailsAsEvent().deletedPhotosIds,
            )

            val syncOperation =
                if (agendaId.isNotEmpty()) SyncOperation.UPDATE else SyncOperation.CREATE
            eventRepository.upsertEvent(event, syncOperation)
                .onSuccess { responseData ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveSuccessful(
                            message = UiText.StringResource(R.string.save_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.SaveError(
                            error = error.asUiText()
                        )
                    )
                }
        }
    }

    private fun deleteTask(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isDeleting = true) }
            taskRepository.deleteTask(id = id)
                .onSuccess {
                    eventChannel.send(
                        AgendaDetailEvent.DeleteSuccessful(
                            message = UiText.StringResource(R.string.delete_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.DeleteError(
                            error = error.asUiText()
                        )
                    )
                }
            _state.update { it.copy(isDeleting = false) }
        }
    }

    private fun deleteReminder(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isDeleting = true) }
            reminderRepository.deleteReminder(id = id)
                .onSuccess {
                    eventChannel.send(
                        AgendaDetailEvent.DeleteSuccessful(
                            message = UiText.StringResource(R.string.delete_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.DeleteError(
                            error = error.asUiText()
                        )
                    )
                }
            _state.update { it.copy(isDeleting = false) }
        }
    }

    private fun deleteEvent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isDeleting = true) }
            eventRepository.deleteEvent(id = id)
                .onSuccess {
                    eventChannel.send(
                        AgendaDetailEvent.DeleteSuccessful(
                            message = UiText.StringResource(R.string.delete_successful)
                        )
                    )
                }
                .onError { error ->
                    eventChannel.send(
                        AgendaDetailEvent.DeleteError(
                            error = error.asUiText()
                        )
                    )
                }
            _state.update { it.copy(isDeleting = false) }
        }
    }

    private fun addAttendee(email: String) {
        viewModelScope.launch {
            updateDetails<AgendaItemDetails.Event> {
                it.copy(isAttendeeOperationInProgress = true)
            }
            eventRepository.getAttendee(email = email)
                .onError { error ->
                    updateDetails<AgendaItemDetails.Event> {
                        it.copy(
                            errors = listOf(
                                ValidationItem(
                                    message = error.asUiText(),
                                    isValid = false,
                                    focusedField = null
                                )
                            ),
                            isAttendeeEmailValid = false
                        )
                    }
                }
                .onSuccess { attendee ->
                    updateDetails<AgendaItemDetails.Event> {
                        it.copy(lookupAttendees = it.lookupAttendees + attendee)
                    }
                    eventChannel.send(
                        AgendaDetailEvent.AttendeeOperationFinish(
                            message = UiText.StringResource(R.string.visitor_added_successfully)
                        )
                    )
                }
            updateDetails<AgendaItemDetails.Event> {
                it.copy(isAttendeeOperationInProgress = false)
            }
        }
    }

    private fun deleteAttendee(userId: String) {
        viewModelScope.launch {
            if (agendaId.isNotEmpty()) {
                eventRepository.deleteAttendee(userId = userId, eventId = agendaId)
            }
            updateDetails<AgendaItemDetails.Event> { event ->
                val updatedLookupAttendees = event.lookupAttendees.filterNot { it.userId == userId }
                val updatedEventAttendees = event.eventAttendees.filterNot { it.userId == userId }

                event.copy(
                    lookupAttendees = updatedLookupAttendees,
                    eventAttendees = updatedEventAttendees
                )
            }
        }
    }
}
