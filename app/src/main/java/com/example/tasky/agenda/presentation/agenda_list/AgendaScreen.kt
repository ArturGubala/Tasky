@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalTime::class
)

package com.example.tasky.agenda.presentation.agenda_list

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.fromEpochMillis
import com.example.tasky.agenda.presentation.util.toInitials
import com.example.tasky.agenda.presentation.util.toKotlinInstant
import com.example.tasky.agenda.presentation.util.toLocal
import com.example.tasky.core.presentation.designsystem.TimeNeedleIndicator
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyFloatingActionButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyProfileButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.cards.TaskyAgendaItemCard
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.dialogs.TaskyModalDialog
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.pickers.HorizontalDatePicker
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import com.example.tasky.core.presentation.util.MenuOptionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime


typealias AgendaItemId = String

@Composable
fun AgendaScreenRoot(
    onSuccessfulLogout: () -> Unit,
    onFabMenuOptionClick: (AgendaKind, AgendaDetailView, String) -> Unit,
    onOpenClick: (AgendaKind, AgendaDetailView, String) -> Unit,
    onEditClick: (AgendaKind, AgendaDetailView, String) -> Unit,
    viewModel: AgendaViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AgendaEvent.LogoutFailure -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            AgendaEvent.LogoutSuccessful -> {
                Toast.makeText(
                    context,
                    R.string.you_are_logged_out,
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulLogout()
            }
            is AgendaEvent.OnFabMenuOptionClick -> {
                onFabMenuOptionClick(
                    event.agendaKind,
                    event.agendaDetailView,
                    event.agendaId
                )
            }
            is AgendaEvent.DeleteAgendaItemFailure -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
                onSuccessfulLogout()
            }
        }
    }

    AgendaScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenClick = onOpenClick,
        onEditClick = onEditClick
    )
}

@Composable
private fun AgendaScreen(
    state: AgendaState,
    onAction: (AgendaAction) -> Unit,
    onOpenClick: (AgendaKind, AgendaDetailView, String) -> Unit,
    onEditClick: (AgendaKind, AgendaDetailView, String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf<AgendaItemId?>(null) }

    TaskyScaffold(
        topBar = {
            TaskyTopAppBar(
                leftActions = {
                    TaskyTextButton(
                        onClick = { onAction(AgendaAction.OnMonthClick) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.selectedDate.month.name.uppercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                rightActions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar_today),
                            contentDescription = "Arrow right icon",
                            modifier = Modifier.clickable { onAction(AgendaAction.OnCalendarIconClick) },
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        TaskyProfileButtonMenu(
                            text = state.userName.toInitials(),
                            onClick = { onAction(AgendaAction.OnProfileButtonClick) },
                            expanded = state.profileMenuExpanded,
                            menuOptions = state.profileButtonMenuOptions,
                            onMenuOptionClick = { option ->
                                when (option.type) {
                                    MenuOptionType.Profile.Logout -> {
                                        onAction(AgendaAction.OnLogoutClick)
                                    }
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
            )
        },
        floatingActionButton = {
            TaskyFloatingActionButtonMenu(
                onClick = { onAction(AgendaAction.OnFabButtonClick) },
                menuOptions = state.fabButtonMenuOptions,
                expanded = state.fabMenuExpanded,
                onMenuOptionClick = { option ->
                    when (option.type) {
                        MenuOptionType.Fab.Event -> onAction(
                            AgendaAction.OnFabMenuOptionClick(
                                agendaKind = AgendaKind.EVENT,
                                agendaDetailView = AgendaDetailView.EDIT
                            )
                        )

                        MenuOptionType.Fab.Task -> onAction(
                            AgendaAction.OnFabMenuOptionClick(
                                agendaKind = AgendaKind.TASK,
                                agendaDetailView = AgendaDetailView.EDIT
                            )
                        )

                        MenuOptionType.Fab.Reminder -> onAction(
                            AgendaAction.OnFabMenuOptionClick(
                                agendaKind = AgendaKind.REMINDER,
                                agendaDetailView = AgendaDetailView.EDIT
                            )
                        )
                    }
                },
                modifier = Modifier.offset(x = 0.dp, y = -(25.dp)),
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            TaskyContentBox(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp)
                ) {
                    HorizontalDatePicker(
                        selectedDate = state.selectedDate.toLocal(),
                        onDateSelected = { date ->
                            onAction(AgendaAction.OnDateSelect(date = date))
                        }
                    )

                    if (state.isLoadingData) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(size = 48.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(
                                items = state.agendaItems,
                                key = { _, item -> item.id },
                                contentType = { _, item -> item.agendaKind }
                            ) { index: Int, item: AgendaItemUi ->
                                val shouldShowNeedleBefore = when {
                                    state.agendaItems.isEmpty() -> false
                                    index == 0 -> now() < item.time.toKotlinInstant()
                                    else -> {
                                        val previousItem = state.agendaItems[index - 1]
                                        now() > previousItem.time.toKotlinInstant() &&
                                                now() < item.time.toKotlinInstant()
                                    }
                                }

                                if (shouldShowNeedleBefore) {
                                    TimeNeedleIndicator(
                                        modifier = Modifier.offset(y = (-6).dp)
                                    )
                                }

                                // Don't know why, but without that delay menu collapse on next screen,
                                // tried few things, nothing works
                                TaskyAgendaItemCard(
                                    title = item.title,
                                    menuOptions = DefaultMenuOptions.getTaskyAgendaItemMenuOptions(),
                                    onMenuOptionClick = { option ->
                                        when (option.type) {
                                            MenuOptionType.AgendaItem.Open -> {
                                                scope.launch {
                                                    onAction(AgendaAction.OnDismissMenu(id = item.id))
                                                    delay(100)
                                                    onOpenClick(
                                                        item.agendaKind,
                                                        AgendaDetailView.READ_ONLY,
                                                        item.id
                                                    )
                                                }
                                            }

                                            MenuOptionType.AgendaItem.Edit -> {
                                                scope.launch {
                                                    onAction(AgendaAction.OnDismissMenu(id = item.id))
                                                    delay(100)
                                                    onEditClick(
                                                        item.agendaKind,
                                                        AgendaDetailView.EDIT,
                                                        item.id
                                                    )
                                                }
                                            }

                                            MenuOptionType.AgendaItem.Delete -> {
                                                showDeleteConfirmationDialog = item.id
                                                onAction(AgendaAction.OnDismissMenu(id = item.id))
                                            }
                                        }
                                    },
                                    dates = item.getFormattedDates(),
                                    expanded = state.expandedMenuItemId == item.id,
                                    onMenuClick = {
                                        onAction(AgendaAction.OnMenuClick(id = item.id))
                                    },
                                    onDismissRequest = {
                                        onAction(AgendaAction.OnDismissMenu(id = item.id))
                                    },
                                    backgroundColor = item.colorProvider.invoke(),
                                    modifier = Modifier
                                        .requiredHeight(125.dp),
                                    isDone = if (item.isCompletable) item.isCompleted else null,
                                    onCompleteTaskClick = {
                                        onAction(
                                            AgendaAction.OnCompleteTaskClick(
                                                id = item.id,
                                                isDone = !item.isCompleted
                                            )
                                        )
                                    },
                                    description = item.description ?: ""
                                )

                                if (index == state.agendaItems.lastIndex &&
                                    now() > item.time.toKotlinInstant()
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))
                                    TimeNeedleIndicator()
                                }
                            }

                            if (state.agendaItems.isEmpty()) {
                                item {
                                    TimeNeedleIndicator()
                                }
                            }
                        }
                    }
                }

                if (state.showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    DatePickerDialog(
                        onDismissRequest = { onAction(AgendaAction.OnDatePickerDismiss) },
                        confirmButton = {
                            TaskyTextButton(
                                onClick = {
                                    val pickedDate =
                                        datePickerState.selectedDateMillis ?: ZonedDateTime.now()
                                            .toEpochSecond()
                                    onAction(
                                        AgendaAction.OnDateSelect(
                                            date = ZonedDateTime.now()
                                                .fromEpochMillis(epochMillis = pickedDate)
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.confirm)
                                )
                            }
                        },
                        dismissButton = {
                            TaskyTextButton(
                                onClick = { onAction(AgendaAction.OnDatePickerDismiss) },
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.cancel)
                                )
                            }
                        }
                    ) {
                        DatePicker(
                            state = datePickerState,
                            title = {
                                Text(
                                    text = stringResource(R.string.choose_date),
                                    modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                                )
                            }
                        )
                    }
                }
            }
        }

        showDeleteConfirmationDialog?.let { itemId ->
            TaskyModalDialog(
                onDismiss = { showDeleteConfirmationDialog = null },
                onConfirm = {
                    onAction(AgendaAction.OnConfirmDeleteClick(id = itemId))
                    showDeleteConfirmationDialog = null
                },
                isConfirmEnable = !state.isDeleting
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AgendaScreenPreview() {
    TaskyTheme {
        AgendaScreen(
            state = AgendaState(),
            onAction = {},
            onOpenClick = { _, _, _ -> },
            onEditClick = { _, _, _ -> }
        )
    }
}
