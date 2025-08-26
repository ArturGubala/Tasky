package com.example.tasky.core.presentation.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class MenuOptionType {
    object Event : MenuOptionType()
    object Task : MenuOptionType()
    object Reminder : MenuOptionType()
    object Logout : MenuOptionType()
}

data class MenuOption(
    val type: MenuOptionType,
    val displayName: String,
    val iconRes: Int? = null,
    val contentDescription: String? = null,
    val iconSize: Dp = 20.dp,
    val onClick: () -> Unit,
    val enable: Boolean = true
)
