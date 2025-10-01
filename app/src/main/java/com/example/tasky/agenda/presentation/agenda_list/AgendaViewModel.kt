@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.presentation.agenda_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.domain.data.TaskyRepository
import com.example.tasky.agenda.domain.data.sync.SyncAgendaItemScheduler
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.toKotlinInstant
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.data.database.event.RoomLocalEventDataSource
import com.example.tasky.core.data.database.reminder.RoomLocalReminderDataSource
import com.example.tasky.core.data.database.task.RoomLocalTaskDataSource
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.util.MenuOptionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class AgendaViewModel(
    private val connectivityObserver: ConnectivityObserver,
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val taskRepository: TaskRepository,
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
        val fabMenuOptions = DefaultMenuOptions.getTaskyFabMenuOptions(
            onEventClick = { onAction(AgendaAction.OnFabMenuOptionClick(
                agendaKind = AgendaKind.EVENT,
                agendaDetailView = AgendaDetailView.EDIT)
            )},
            onTaskClick = { onAction(AgendaAction.OnFabMenuOptionClick(
                agendaKind = AgendaKind.TASK,
                agendaDetailView = AgendaDetailView.EDIT)
            )},
            onReminderClick = { onAction(AgendaAction.OnFabMenuOptionClick(
                agendaKind = AgendaKind.REMINDER,
                agendaDetailView = AgendaDetailView.EDIT)
            )}
        )

        val profileMenuOptions = DefaultMenuOptions.getTaskyProfileMenuOptions(
            onLogoutClick = { onAction(AgendaAction.OnLogoutClick) }
        )

        _state.update {
            it.copy(
                fabButtonMenuOptions = fabMenuOptions,
                profileButtonMenuOptions = profileMenuOptions
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
        // TODO just for test before implement date picking
        val startOfDay = 1758326400000L
        val endOfDay = 1759976799000L

        combine(
            localTaskDataSource.getTasksForDay(startOfDay, endOfDay),
            localReminderDataSource.getReminderForDay(startOfDay, endOfDay),
            localEventDataSource.getEventForDay(startOfDay, endOfDay)
        ) { tasks, reminders, events ->
            val allItems = buildList {
                addAll(tasks.map { AgendaItemUi.TaskItem(it) })
                addAll(reminders.map { AgendaItemUi.ReminderItem(it) })
                addAll(events.map { AgendaItemUi.EventItem(it) })
            }
            allItems.sortedBy { it.time.toKotlinInstant().toEpochMilliseconds() }
        }.onEach { sortedAgendaItems ->
            _state.update { it.copy(agendaItems = sortedAgendaItems) }
        }.launchIn(viewModelScope)
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
                is MenuOptionType.Logout -> {
                    option.copy(enable = _state.value.canLogout)
                }
                else -> option
            }
        }

        _state.update {
            it.copy(
                profileButtonMenuOptions = updatedProfileOptions
            )
        }
    }
}
