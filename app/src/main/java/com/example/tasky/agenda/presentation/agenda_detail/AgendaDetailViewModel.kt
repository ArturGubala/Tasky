package com.example.tasky.agenda.presentation.agenda_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.presentation.agenda_list.AgendaAction
import com.example.tasky.agenda.presentation.agenda_list.AgendaEvent
import com.example.tasky.core.domain.util.ConnectivityObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AgendaDetailViewModel(
    private val agendaId: String,
    private val connectivityObserver: ConnectivityObserver
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
            _state.update { it.copy(loadingInitialData = false) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaDetailState(),
        )
    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AgendaAction) {}
}
