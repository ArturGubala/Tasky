package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.core.presentation.ui.UiText

interface AgendaDetailEvent {
    data class ImageCompressFailure(val error: UiText): AgendaDetailEvent
    data class ImageTooLarge(val error: UiText): AgendaDetailEvent
    data class InvalidDatePicked(val error: UiText): AgendaDetailEvent
}
