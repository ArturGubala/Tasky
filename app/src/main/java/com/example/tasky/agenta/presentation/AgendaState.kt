package com.example.tasky.agenta.presentation

import com.example.tasky.core.presentation.util.MenuOption

data class AgendaState(
    val canLogout: Boolean = false,
    val fabButtonMenuOptions: List<MenuOption> = listOf(),
    val profileButtonMenuOptions: List<MenuOption> = listOf()
)
