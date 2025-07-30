package com.example.tasky.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.R
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.Result
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            RegisterState(),
        )

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: RegisterAction) {
        when(action) {
            RegisterAction.OnLoginClick -> onLoginCLick()
            RegisterAction.OnRegisterClick -> registerUser()
            RegisterAction.OnTogglePasswordVisibilityClick -> changePasswordVisibility()
        }
    }

    private fun changePasswordVisibility() {
        _state.update {
            it.copy(
                isPasswordVisible = !it.isPasswordVisible
            )
        }
    }

    private fun onLoginCLick() {
        viewModelScope.launch {
            eventChannel.send(RegisterEvent.OnLoginClick)
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            _state.update { it.copy(isRegistering = true) }
            val result = authRepository.register(
                fullName = _state.value.name.trim(),
                email = _state.value.email.trim(),
                password = _state.value.password.text.toString()
            )
            _state.update { it.copy(isRegistering = false) }

            when(result) {
                is Result.Error -> {
                    if(result.error == DataError.Network.CONFLICT) {
                        eventChannel.send(RegisterEvent.RegistrationFailure(
                            error = UiText.StringResource(R.string.error_email_exists)
                        ))
                    } else {
                        eventChannel.send(RegisterEvent.RegistrationFailure(
                            error = result.error.asUiText()
                        ))
                    }
                }
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
            }
        }
    }
}
