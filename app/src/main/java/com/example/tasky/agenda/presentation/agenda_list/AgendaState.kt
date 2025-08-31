package com.example.tasky.agenda.presentation.agenda_list

import com.example.tasky.core.presentation.util.MenuOption

data class AgendaState(
    val canLogout: Boolean = false,
    val fabMenuExpanded: Boolean = false,
    val profileMenuExpanded: Boolean = false,
    val fabButtonMenuOptions: List<MenuOption> = listOf(),
    val profileButtonMenuOptions: List<MenuOption> = listOf()
)
