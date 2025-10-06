@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.tasky.agenda.presentation.agenda_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda.domain.data.EventRepository
import com.example.tasky.agenda.domain.data.ReminderRepository
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.toKotlinInstant
import com.example.tasky.agenda.presentation.util.toUtc
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.data.database.SyncOperation
import com.example.tasky.core.data.database.event.RoomLocalEventDataSource
import com.example.tasky.core.data.database.reminder.RoomLocalReminderDataSource
import com.example.tasky.core.data.database.task.RoomLocalTaskDataSource
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.domain.util.onError
import com.example.tasky.core.domain.util.onSuccess
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import com.example.tasky.core.presentation.util.MenuOptionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class AgendaViewModel(
    private val connectivityObserver: ConnectivityObserver,
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
    private val reminderRepository: ReminderRepository,
    private val taskyRepository: TaskyRepository,
    private val syncAgendaItemScheduler: SyncAgendaItemScheduler,
    private val localTaskDataSource: RoomLocalTaskDataSource,
    private val localReminderDataSource: RoomLocalReminderDataSource,
    private val localEventDataSource: RoomLocalEventDataSource,
) : ViewModel() {

    private var initialized = false
    private val _state = MutableStateFlow(AgendaState())
    val state = _state
        .onStart {
            if (!initialized) {
                initializeData()
                initialized = true
            }
            initializeMenuOptions()
            observeConnectivity()
            observeAgendaItems()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaState(),
        )
    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun initializeMenuOptions() {
        _state.update {
            it.copy(
                fabButtonMenuOptions = DefaultMenuOptions.getTaskyFabMenuOptions(),
                profileButtonMenuOptions = DefaultMenuOptions.getTaskyProfileMenuOptions()
            )
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.isConnected
            .onEach { isConnected ->
                _state.update {
                    it.copy(
                        canLogout = isConnected
                    )
                }
                updateMenuOptionsForConnectivity()
            }
            .launchIn(viewModelScope)
    }


    private fun initializeData() {
        viewModelScope.launch {
            val authInfo = sessionStorage.get()
            _state.update {
                it.copy(userName = authInfo?.userName ?: "")
            }
            syncAgendaItemScheduler.scheduleSync(
                type = SyncAgendaItemScheduler.SyncType.FetchAgendaItems(
                    interval = 30.minutes
                )
            )
            taskRepository.syncPendingTask()
            taskyRepository.fetchFullAgenda()

        }
    }

    private fun observeAgendaItems() {
        _state
            .map { it.selectedDate }
            .distinctUntilChanged()
            .flatMapLatest { selectedDateUtc ->
                val selectedDateLocal = selectedDateUtc.withZoneSameInstant(ZoneId.systemDefault())

                val startOfDayLocal = selectedDateLocal.toLocalDate()
                    .atStartOfDay(ZoneId.systemDefault())
                val endOfDayLocal = startOfDayLocal.plusDays(1).minusNanos(1)

                val startOfDayUtc = startOfDayLocal.withZoneSameInstant(ZoneId.of("UTC"))
                val endOfDayUtc = endOfDayLocal.withZoneSameInstant(ZoneId.of("UTC"))

                val startOfDayMillis = startOfDayUtc.toInstant().toEpochMilli()
                val endOfDayMillis = endOfDayUtc.toInstant().toEpochMilli()

                combine(
                    localTaskDataSource.getTasksForDay(startOfDayMillis, endOfDayMillis),
                    localReminderDataSource.getReminderForDay(startOfDayMillis, endOfDayMillis),
                    localEventDataSource.getEventForDay(startOfDayMillis, endOfDayMillis)
                ) { tasks, reminders, events ->
                    val allItems = buildList {
                        addAll(tasks.map { AgendaItemUi.TaskItem(it) })
                        addAll(reminders.map { AgendaItemUi.ReminderItem(it) })
                        addAll(events.map { AgendaItemUi.EventItem(it) })
                    }
                    allItems.sortedBy { it.time.toKotlinInstant().toEpochMilliseconds() }
                }
            }
            .onEach { sortedAgendaItems ->
                _state.update { it.copy(agendaItems = sortedAgendaItems) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AgendaAction) {
        when(action) {
            AgendaAction.OnLogoutClick -> logout()
            AgendaAction.OnFabButtonClick -> {
                _state.update { it.copy(fabMenuExpanded = !_state.value.fabMenuExpanded) }
            }

            AgendaAction.OnProfileButtonClick -> {
                _state.update { it.copy(profileMenuExpanded = !_state.value.profileMenuExpanded) }
            }

            is AgendaAction.OnFabMenuOptionClick -> {
                _state.update { it.copy(fabMenuExpanded = false) }
                viewModelScope.launch {
                    // Don't know why, but without that delay menu collapse on next screen,
                    // tried few things, nothing works
                    delay(100)
                    eventChannel.send(
                        AgendaEvent.OnFabMenuOptionClick(
                            agendaKind = action.agendaKind,
                            agendaDetailView = action.agendaDetailView,
                            agendaId = action.agendaId
                        )
                    )
                }
            }
            is AgendaAction.OnConfirmDeleteClick -> deleteAgendaItem(id = action.id)
            is AgendaAction.OnMenuClick -> {
                _state.update { it.copy(expandedMenuItemId = action.id) }
            }

            is AgendaAction.OnDismissMenu -> {
                if (_state.value.expandedMenuItemId == action.id) {
                    _state.update { it.copy(expandedMenuItemId = null) }
                }
            }

            is AgendaAction.OnCompleteTaskClick -> updateAgendaItem(
                id = action.id,
                isDone = action.isDone
            )
            is AgendaAction.OnDateSelect -> {
                Timber.log(1, _state.value.selectedDate.toString())
                _state.update { it.copy(selectedDate = action.date.toUtc()) }
                if (_state.value.showDatePicker) {
                    _state.update { it.copy(showDatePicker = false) }
                }
            }

            AgendaAction.OnDatePickerDismiss -> {
                _state.update { it.copy(showDatePicker = false) }
            }

            AgendaAction.OnCalendarIconClick,
            AgendaAction.OnMonthClick,
                -> {
                _state.update { it.copy(showDatePicker = true) }
            }
        }
    }

    private fun logout() {
        _state.update { it.copy(profileMenuExpanded = false) }
        viewModelScope.launch {
            val authInfo = sessionStorage.get() ?: run {
                eventChannel.send(AgendaEvent.LogoutFailure(
                    error = UiText.StringResource(R.string.logged_out_error)
                ))
                return@launch
            }

            val result = authRepository.logout(refreshToken = authInfo.refreshToken)

            when(result) {
                is Result.Error -> {
                    eventChannel.send(AgendaEvent.LogoutFailure(
                        error = UiText.StringResource(R.string.logged_out_error)
                    ))
                }
                is Result.Success -> {
                    taskyRepository.cleanUpLocalData()
                    eventChannel.send(AgendaEvent.LogoutSuccessful)
                }
            }
        }
    }

    private fun updateMenuOptionsForConnectivity() {
        val updatedProfileOptions = _state.value.profileButtonMenuOptions.map { option ->
            when (option.type) {
                is MenuOptionType.Profile.Logout -> {
                    option.copy(enable = _state.value.canLogout)
                }
            }
        }

        _state.update {
            it.copy(
                profileButtonMenuOptions = updatedProfileOptions
            )
        }
    }

    private fun deleteAgendaItem(id: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isDeleting = true)
            }
            val agendaItemToDelete =
                _state.value.agendaItems.firstOrNull { it.id == id }
            if (agendaItemToDelete == null) {
                _state.update {
                    it.copy(isDeleting = false)
                }
                return@launch
            }

            val result = when (agendaItemToDelete.agendaKind) {
                AgendaKind.TASK -> taskRepository.deleteTask(id = agendaItemToDelete.id)
                AgendaKind.EVENT -> eventRepository.deleteEvent(id = agendaItemToDelete.id)
                AgendaKind.REMINDER -> reminderRepository.deleteReminder(id = agendaItemToDelete.id)
            }

            result
                .onError { error ->
                    eventChannel.send(AgendaEvent.LogoutFailure(error = error.asUiText()))
                }
                .onSuccess {
                    _state.update {
                        it.copy(isDeleting = false)
                    }
                }
        }
    }

    private fun updateAgendaItem(id: String, isDone: Boolean) {
        viewModelScope.launch {
            val task = localTaskDataSource.getTask(id).firstOrNull()
            if (task == null) return@launch

            taskRepository.upsertTask(
                task = task.copy(
                    updatedAt = ZonedDateTime.now(ZoneId.of("UTC")).toKotlinInstant(),
                    isDone = isDone
                ),
                syncOperation = SyncOperation.UPDATE
            )
        }
    }
}
