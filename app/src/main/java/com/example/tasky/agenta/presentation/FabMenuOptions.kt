package com.example.tasky.agenta.presentation

import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.util.MenuOption
import com.example.tasky.core.presentation.util.MenuOptionType

object DefaultMenuOptions {
    fun getTaskyMenuOptions(
        onEventClick: () -> Unit = {},
        onTaskClick: () -> Unit = {},
        onReminderClick: () -> Unit = {}
    ): List<MenuOption> = listOf(
        MenuOption(
            type = MenuOptionType.Event,
            displayName = "Event",
            iconRes = R.drawable.ic_calendar_today,
            contentDescription = "Create event",
            size = 20.dp,
            onClick = onEventClick
        ),
        MenuOption(
            type = MenuOptionType.Task,
            displayName = "Task",
            iconRes = R.drawable.ic_check,
            contentDescription = "Create task",
            size = 20.dp,
            onClick = onTaskClick
        ),
        MenuOption(
            type = MenuOptionType.Reminder,
            displayName = "Reminder",
            iconRes = R.drawable.ic_bell,
            contentDescription = "Create reminder",
            size = 20.dp,
            onClick = onReminderClick
        )
    )
}
