package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.core.presentation.util.MenuOption
import java.time.ZoneId
import java.time.ZonedDateTime

data class AgendaState(
    val isLoadingData: Boolean = false,
    val canLogout: Boolean = false,
    val fabMenuExpanded: Boolean = false,
    val profileMenuExpanded: Boolean = false,
    val fabButtonMenuOptions: List<MenuOption> = listOf(),
    val profileButtonMenuOptions: List<MenuOption> = listOf(),
    val agendaItems: List<AgendaItemUi> = listOf(),
    val isModalDialogVisible: Boolean = false,
    val agendaItemIdToDelete: String? = null,
    val isDeleting: Boolean = false,
    val expandedMenuItemId: String? = null,
    val selectedDate: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    val showDatePicker: Boolean = false,
    val userName: String = "",
)
