@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.example.tasky.agenda.presentation.agenda_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.agenda.domain.util.AgendaKind
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyFloatingActionButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyProfileButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.dialogs.TaskyModalDialog
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

// TODO test implementation for testing navigation and context menu
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
    TaskyScaffold(
        topBar = {
            TaskyTopAppBar(
                leftActions = {
                    TaskyTextButton(
                        onClick = {}
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "MARCH",
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
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        TaskyProfileButtonMenu(
                            text = "AG",
                            onClick = { onAction(AgendaAction.OnProfileButtonClick) },
                            expanded = state.profileMenuExpanded,
                            menuOptions = state.profileButtonMenuOptions
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
                modifier = Modifier.offset(x = 0.dp, y = -(25.dp))
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn {
                        items(state.agendaItems) { item ->
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = item.agendaKind.name,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Text(
                                            text = item.id,
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                        Text(
                                            text = item.title,
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            onOpenClick(
                                                item.agendaKind,
                                                AgendaDetailView.READ_ONLY,
                                                item.id
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = "Open",
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                    }
                                    Button(
                                        onClick = {
                                            onEditClick(
                                                item.agendaKind,
                                                AgendaDetailView.EDIT,
                                                item.id
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = "Edit",
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                    }
                                    Button(
                                        onClick = { onAction(AgendaAction.OnDeleteMenuOptionClick(id = item.id)) }
                                    ) {
                                        Text(
                                            text = "Delete",
                                            color = MaterialTheme.colorScheme.extended.success
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.isModalDialogVisible) {
            TaskyModalDialog(
                onDismiss = { onAction(AgendaAction.OnDismissModalDialog) },
                onConfirm = { onAction(AgendaAction.OnConfirmDeleteClick) },
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
            onOpenClick = {} as (AgendaKind, AgendaDetailView, String) -> Unit,
            onEditClick = {} as (AgendaKind, AgendaDetailView, String) -> Unit
        )
    }
}
