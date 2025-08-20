package com.example.tasky.core.presentation.util

import androidx.compose.ui.unit.Dp

sealed class MenuOptionType {
    object Event : MenuOptionType()
    object Task : MenuOptionType()
    object Reminder : MenuOptionType()
}

data class MenuOption(
    val type: MenuOptionType,
    val displayName: String,
    val iconRes: Int,
    val contentDescription: String,
    val size: Dp,
    val onClick: () -> Unit
)
