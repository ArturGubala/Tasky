package com.example.tasky.agenda.presentation.agenda_list

import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.ui.UiText
import com.example.tasky.core.presentation.util.MenuOption
import com.example.tasky.core.presentation.util.MenuOptionType

object DefaultMenuOptions {
    internal fun getTaskyFabMenuOptions(): List<MenuOption<MenuOptionType.Fab>> = listOf(
        MenuOption(
            type = MenuOptionType.Fab.Event,
            displayName = UiText.StringResource(R.string.event),
            iconRes = R.drawable.ic_calendar_today,
            contentDescription = UiText.StringResource(R.string.create_event),
            iconSize = 20.dp
        ),
        MenuOption(
            type = MenuOptionType.Fab.Task,
            displayName = UiText.StringResource(R.string.task),
            iconRes = R.drawable.ic_check,
            contentDescription = UiText.StringResource(R.string.create_task),
            iconSize = 20.dp
        ),
        MenuOption(
            type = MenuOptionType.Fab.Reminder,
            displayName = UiText.StringResource(R.string.reminder),
            iconRes = R.drawable.ic_bell,
            contentDescription = UiText.StringResource(R.string.create_reminder),
            iconSize = 20.dp
        )
    )

    internal fun getTaskyProfileMenuOptions(): List<MenuOption<MenuOptionType.Profile>> = listOf(
        MenuOption(
            type = MenuOptionType.Profile.Logout,
            displayName = UiText.StringResource(R.string.log_out),
            iconRes = R.drawable.ic_offline,
            contentDescription = UiText.StringResource(R.string.offline_icon),
            iconSize = 20.dp
        )
    )

    internal fun getTaskyAgendaItemMenuOptions(): List<MenuOption<MenuOptionType.AgendaItem>> =
        listOf(
        MenuOption(
            type = MenuOptionType.AgendaItem.Open,
            displayName = UiText.StringResource(R.string.open),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.open_item),
            iconSize = 20.dp
        ),
        MenuOption(
            type = MenuOptionType.AgendaItem.Edit,
            displayName = UiText.StringResource(R.string.edit),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.edit_item),
            iconSize = 20.dp
        ),
        MenuOption(
            type = MenuOptionType.AgendaItem.Delete,
            displayName = UiText.StringResource(R.string.delete),
            iconRes = null,
            contentDescription = UiText.StringResource(R.string.delete_item),
            iconSize = 20.dp
        )
    )
}
