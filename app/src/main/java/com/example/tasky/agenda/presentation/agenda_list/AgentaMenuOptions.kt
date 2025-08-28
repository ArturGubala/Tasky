package com.example.tasky.agenda.presentation.agenda_list

import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.util.MenuOption
import com.example.tasky.core.presentation.util.MenuOptionType

object DefaultMenuOptions {
    internal fun getTaskyFabMenuOptions(
        onEventClick: () -> Unit = {},
        onTaskClick: () -> Unit = {},
        onReminderClick: () -> Unit = {}
    ): List<MenuOption> = listOf(
        MenuOption(
            type = MenuOptionType.Event,
            displayName = "Event",
            iconRes = R.drawable.ic_calendar_today,
            contentDescription = "Create event",
            iconSize = 20.dp,
            onClick = onEventClick
        ),
        MenuOption(
            type = MenuOptionType.Task,
            displayName = "Task",
            iconRes = R.drawable.ic_check,
            contentDescription = "Create task",
            iconSize = 20.dp,
            onClick = onTaskClick
        ),
        MenuOption(
            type = MenuOptionType.Reminder,
            displayName = "Reminder",
            iconRes = R.drawable.ic_bell,
            contentDescription = "Create reminder",
            iconSize = 20.dp,
            onClick = onReminderClick
        )
    )

    internal fun getTaskyProfileMenuOptions(
        onLogoutClick: () -> Unit = {}
    ): List<MenuOption> = listOf(
        MenuOption(
            type = MenuOptionType.Logout,
            displayName = "Log out",
            iconRes = R.drawable.ic_offline,
            contentDescription = "Offline icon",
            iconSize = 20.dp,
            onClick = onLogoutClick
        )
    )
}
