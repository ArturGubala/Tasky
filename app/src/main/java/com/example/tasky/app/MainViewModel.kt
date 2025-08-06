package com.example.tasky.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.core.data.datastore.EncryptedSessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    sessionStorage: EncryptedSessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isCheckingAuth = true) }
            _state.update { it.copy(isLoggedIn = sessionStorage.get() != null) }
            _state.update { it.copy(isCheckingAuth = false) }
        }
    }
}
