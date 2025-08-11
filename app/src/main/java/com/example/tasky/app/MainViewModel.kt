package com.example.tasky.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.domain.AuthState
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeParseException

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isCheckingAuth = true) }

            val authInfo = sessionStorage.get()
            val authState = when {
                authInfo == null -> AuthState.NOT_AUTHENTICATED
                isTokenExpired(authInfo.accessTokenExpirationTimestamp) -> {
                    refreshToken(authInfo.refreshToken)
                }
                else -> AuthState.AUTHENTICATED
            }

            _state.update {
                it.copy(
                    authState = authState,
                    isCheckingAuth = false
                )
            }
        }
    }

    private fun refreshToken(refreshToken: String): AuthState {
        var resultAuthState = AuthState.TOKEN_EXPIRED

        viewModelScope.launch {
            val result = authRepository.refreshToken(refreshToken = refreshToken)

            resultAuthState = when(result) {
                is Result.Error -> {
                    AuthState.TOKEN_EXPIRED
                }
                is Result.Success -> {
                    AuthState.AUTHENTICATED
                }
            }
        }

        return resultAuthState
    }

    private fun isTokenExpired(tokenExpirationTimestamp: String): Boolean {
        return try {
            val parsedExpirationTimestamp = Instant.parse(tokenExpirationTimestamp)
            val currentTimestamp = Instant.now()

            currentTimestamp.isAfter(parsedExpirationTimestamp)
        } catch (_: DateTimeParseException) {
            true
        }
    }
}
