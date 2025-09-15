package com.example.tasky.agenda.presentation.agenda_edit

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AgendaEditTextViewModel(
    text: String,
) : ViewModel() {

    private val _state = MutableStateFlow(AgendaEditTextState(
        textFieldState = TextFieldState(initialText = text)
    ))
    val state = _state.asStateFlow()
    private val eventChannel = Channel<AgendaEditTextEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: AgendaEditTextAction) {
        when (action) {
            AgendaEditTextAction.OnCancelClick -> {
                viewModelScope.launch {
                    eventChannel.send(AgendaEditTextEvent.OnReadyAfterCancelClick)
                }
            }
            is AgendaEditTextAction.OnSaveClick -> {
                viewModelScope.launch {
                    eventChannel.send(
                        AgendaEditTextEvent.OnReadyAfterSaveClick(
                            text = _state.value.textFieldState.text.toString()
                        )
                    )
                }
            }
        }
    }
}
