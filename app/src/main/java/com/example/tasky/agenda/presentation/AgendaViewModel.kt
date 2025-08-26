package com.example.tasky.agenda.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.util.MenuOptionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgendaViewModel(
    private val connectivityObserver: ConnectivityObserver,
    private val authRepository: AuthRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaState())
    val state = _state.asStateFlow()
    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        initializeMenuOptions()
        observeConnectivity()
    }

    private fun initializeMenuOptions() {
        val fabMenuOptions = DefaultMenuOptions.getTaskyFabMenuOptions(
            onEventClick = { onAction(AgendaAction.OnEventClick) },
            onTaskClick = { onAction(AgendaAction.OnTaskClick) },
            onReminderClick = { onAction(AgendaAction.OnReminderClick) }
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
        }
    }

    private fun logout() {
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
