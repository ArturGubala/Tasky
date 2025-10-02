package com.example.tasky.agenda.presentation.agenda_list

import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.ui.UiText
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
            displayName = UiText.StringResource(R.string.event),
            iconRes = R.drawable.ic_calendar_today,
            contentDescription = UiText.StringResource(R.string.create_event),
            iconSize = 20.dp,
            onClick = onEventClick
        ),
        MenuOption(
            type = MenuOptionType.Task,
            displayName = UiText.StringResource(R.string.task),
            iconRes = R.drawable.ic_check,
            contentDescription = UiText.StringResource(R.string.create_task),
            iconSize = 20.dp,
            onClick = onTaskClick
        ),
        MenuOption(
            type = MenuOptionType.Reminder,
            displayName = UiText.StringResource(R.string.reminder),
            iconRes = R.drawable.ic_bell,
            contentDescription = UiText.StringResource(R.string.create_reminder),
            iconSize = 20.dp,
            onClick = onReminderClick
        )
    )

    internal fun getTaskyProfileMenuOptions(
        onLogoutClick: () -> Unit = {}
    ): List<MenuOption> = listOf(
        MenuOption(
            type = MenuOptionType.Logout,
            displayName = UiText.StringResource(R.string.log_out),
            iconRes = R.drawable.ic_offline,
            contentDescription = UiText.StringResource(R.string.offline_icon),
            iconSize = 20.dp,
            onClick = onLogoutClick
        )
    )

    internal fun getTaskyAgendaItemMenuOptions(
        onOpenClick: () -> Unit = {},
        onEditClick: () -> Unit = {},
        onDeleteClick: () -> Unit = {},
    ): List<MenuOption> = listOf(
        MenuOption(
            type = MenuOptionType.CardMenuOption,
            displayName = UiText.StringResource(R.string.open),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.create_event),
            iconSize = 20.dp,
            onClick = onOpenClick
        ),
        MenuOption(
            type = MenuOptionType.CardMenuOption,
            displayName = UiText.StringResource(R.string.edit),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.create_task),
            iconSize = 20.dp,
            onClick = onEditClick
        ),
        MenuOption(
            type = MenuOptionType.CardMenuOption,
            displayName = UiText.StringResource(R.string.delete),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.create_reminder),
            iconSize = 20.dp,
            onClick = onDeleteClick
        )
    )
}
