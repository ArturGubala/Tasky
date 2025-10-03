package com.example.tasky.core.presentation.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.ui.UiText

sealed class MenuOptionType {
    sealed class Fab : MenuOptionType() {
        data object Event : Fab()
        data object Task : Fab()
        data object Reminder : Fab()
    }

    sealed class Profile : MenuOptionType() {
        data object Logout : Profile()
    }

    sealed class AgendaItem : MenuOptionType() {
        data object Open : AgendaItem()
        data object Edit : AgendaItem()
        data object Delete : AgendaItem()
    }
}

data class MenuOption<T>(
    val type: T,
    val displayName: UiText,
    val iconRes: Int? = null,
    val contentDescription: UiText? = null,
    val iconSize: Dp = 20.dp,
    val enable: Boolean = true,
)
