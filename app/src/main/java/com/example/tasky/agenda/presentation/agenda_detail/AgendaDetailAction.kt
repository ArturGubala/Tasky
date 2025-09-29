package com.example.tasky.agenda.presentation.agenda_detail

import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaItemAttendeesStatus
import com.example.tasky.agenda.presentation.util.AgendaItemInterval

interface AgendaDetailAction {
    data class OnAgendaItemIntervalSelect(val reminder: AgendaItemInterval): AgendaDetailAction
    data class OnTimeFromPick(val hour: Int, val minute: Int): AgendaDetailAction
    data class OnDateFromPick(val dateMillis: Long): AgendaDetailAction
    data class OnTimeToPick(val hour: Int, val minute: Int): AgendaDetailAction
    data class OnDateToPick(val dateMillis: Long): AgendaDetailAction
    data class OnAttendeeStatusClick(val status: AgendaItemAttendeesStatus): AgendaDetailAction
    data class OnEditTitleClick(val title: String): AgendaDetailAction
    data class OnEditDescriptionClick(val description: String): AgendaDetailAction
    data class OnTitleChange(val title: String): AgendaDetailAction
    data class OnDescriptionChange(val description: String): AgendaDetailAction
    data object OnCancelClick: AgendaDetailAction
    data object OnCloseClick: AgendaDetailAction
    data object OnEditClick: AgendaDetailAction
    data object OnAddPhotoClick: AgendaDetailAction
    data class OnPhotoSelected(val uriString: String, val maxBytes: Int): AgendaDetailAction
    data class OnPhotoClick(val photoId: String, val uriString: String): AgendaDetailAction
    data class OnPhotoDelete(val photoId: String): AgendaDetailAction
    data object OnAddAttendeeClick: AgendaDetailAction
    data object OnDeleteAgendaItemClick: AgendaDetailAction
    data object OnDismissBottomSheet: AgendaDetailAction
    data class OnAttendeeEmailValueChanged(val email: String) : AgendaDetailAction
    data class OnAttendeeEmailFieldFocusChanged(val hasFocus: Boolean) : AgendaDetailAction
    data class OnSaveClick(val agendaKind: AgendaKind) : AgendaDetailAction
    data class OnDeleteOnBottomSheetClick(val id: String, val agendaKind: AgendaKind) :
        AgendaDetailAction
    data class OnAddOnBottomSheetClick(val email: String) :
        AgendaDetailAction
}
