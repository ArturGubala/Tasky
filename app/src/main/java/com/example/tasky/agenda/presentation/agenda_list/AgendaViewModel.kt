package com.example.tasky.agenda.presentation.agenda_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.agenda.domain.data.TaskRepository
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.util.MenuOptionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgendaViewModel(
    private val connectivityObserver: ConnectivityObserver,
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage,
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaState())
    val state = _state
        .onStart {
            taskRepository.syncPendingTask()
            initializeMenuOptions()
            observeConnectivity()
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
                agendaItemType = AgendaItemType.EVENT,
                agendaDetailView = AgendaDetailView.EDIT)
            )},
            onTaskClick = { onAction(AgendaAction.OnFabMenuOptionClick(
                agendaItemType = AgendaItemType.TASK,
                agendaDetailView = AgendaDetailView.EDIT)
            )},
            onReminderClick = { onAction(AgendaAction.OnFabMenuOptionClick(
                agendaItemType = AgendaItemType.REMINDER,
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
                    // TODO: Don't know why, but without that delay menu collapse on next screen, tried few things, nothing works
                    delay(100)
                    eventChannel.send(
                        AgendaEvent.OnFabMenuOptionClick(
                            agendaItemType = action.agendaItemType,
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
