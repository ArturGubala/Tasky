package com.example.tasky.core.presentation.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.ui.UiText

sealed class MenuOptionType {
    object Event : MenuOptionType()
    object Task : MenuOptionType()
    object Reminder : MenuOptionType()
    object Logout : MenuOptionType()
    object CardMenuOption : MenuOptionType()
}

data class MenuOption(
    val type: MenuOptionType,
    val displayName: UiText,
    val iconRes: Int? = null,
    val contentDescription: UiText? = null,
    val iconSize: Dp = 20.dp,
    val onClick: () -> Unit,
    val enable: Boolean = true,
)
