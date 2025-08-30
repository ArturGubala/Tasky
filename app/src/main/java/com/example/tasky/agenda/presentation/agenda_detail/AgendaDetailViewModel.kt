package com.example.tasky.agenda.presentation.agenda_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda.presentation.agenda_list.AgendaAction
import com.example.tasky.agenda.presentation.agenda_list.AgendaEvent
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfigProvider
import com.example.tasky.core.domain.util.ConnectivityObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn


class AgendaDetailViewModel(
    private val agendaDetailView: AgendaDetailView,
    private val agendaItemType: AgendaItemType,
    private val agendaId: String?,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaDetailState(
        agendaItemType = agendaItemType,
        agendaDetailView = agendaDetailView,
        agendaDetailConfig = AgendaTypeConfigProvider.getConfig(type = agendaItemType)
    ))
    val state = _state
        .onStart {
            if (agendaDetailView == AgendaDetailView.EDIT && agendaId != null) {
                // TODO: Get agenda details from db by provided id
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AgendaDetailState(
                agendaItemType = agendaItemType,
                agendaDetailView = agendaDetailView,
                agendaDetailConfig = AgendaTypeConfigProvider.getConfig(type = agendaItemType)
            ),
        )
    private val eventChannel = Channel<AgendaEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AgendaAction) {}
}
