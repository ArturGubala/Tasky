@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.agenda.presentation.util.AgendaDetailConfigProvider
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.agenda.presentation.util.AgendaItemType
import com.example.tasky.agenda.presentation.util.AgendaTypeConfig
import com.example.tasky.agenda.presentation.util.defaultAgendaItemIntervals
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.drop_downs.TaskyAgendaItemDropdownMenu
import com.example.tasky.core.presentation.designsystem.icons.TaskyCircle
import com.example.tasky.core.presentation.designsystem.icons.TaskySquare
import com.example.tasky.core.presentation.designsystem.labels.TaskyLabel
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.pickers.TaskyDatePicker
import com.example.tasky.core.presentation.designsystem.pickers.TaskyTimePicker
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.util.DateTimeFormatter
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AgendaDetailScreenRoot(
    agendaItemType: AgendaItemType,
    agendaDetailView: AgendaDetailView,
    agendaId: String = "",
    onBackClick: () -> Unit,
    viewModel: AgendaDetailViewModel = koinViewModel(
        parameters = { parametersOf(agendaId) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val agendaItemTypeConfiguration by remember(agendaItemType) {
        mutableStateOf(AgendaDetailConfigProvider.getConfig(type = agendaItemType))
    }

    AgendaDetailScreen(
        state = state,
        viewModel::onAction,
        appBarTitle = agendaItemTypeConfiguration.getAppBarTitle(
            mode = agendaDetailView,
            context = context,
            itemDate = null
        ),
        agendaDetailView = agendaDetailView,
        agendaItemTypeConfiguration = agendaItemTypeConfiguration
    )
}

@Composable
fun AgendaDetailScreen(
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
    appBarTitle: String,
    agendaDetailView: AgendaDetailView,
    agendaItemTypeConfiguration: AgendaTypeConfig,
) {
    val isReadOnly = agendaDetailView == AgendaDetailView.READ_ONLY

    TaskyScaffold(
        topBar = {
            when(agendaDetailView) {
                AgendaDetailView.READ_ONLY -> {
                    TaskyTopAppBar(
                        leftActions = {
                            TaskyTextButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        rightActions = {
                            TaskyTextButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit icon",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        title = {
                            Text(
                                text = appBarTitle.uppercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                AgendaDetailView.EDIT -> {
                    TaskyTopAppBar(
                        leftActions = {
                            TaskyTextButton(
                                onClick = {}
                            ) {
                                Text(
                                    text = stringResource(R.string.cancel),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        },
                        rightActions = {
                            TaskyTextButton(
                                onClick = {}
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    color = MaterialTheme.colorScheme.extended.success,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        title = {
                            Text(
                                text = appBarTitle.uppercase(),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
        }
    ) { padding ->
        TaskyContentBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopStart
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TaskyLabel(
                        text = agendaItemTypeConfiguration.displayName.uppercase(),
                        textStyle = MaterialTheme.typography.labelMedium,
                        modifier = Modifier,
                        labelLeadingIcon = {
                            TaskySquare(
                                size = 20.dp,
                                color = agendaItemTypeConfiguration.getColor(),
                                modifier = Modifier
                                    .then(
                                        agendaItemTypeConfiguration.getStrokeColor()?.let { strokeColor ->
                                            Modifier.border(
                                                width = 1.dp,
                                                color = strokeColor,
                                                shape = RoundedCornerShape(4.dp))
                                        } ?: Modifier
                                    )
                            )
                        }
                    )
                }

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        TaskyLabel(
                            text = "Project X",
                            textStyle = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier,
                            labelLeadingIcon = {
                                TaskyCircle(
                                    size = 20.dp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                )
                            }
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.extended.surfaceHigher
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                    ) {
                        Text(
                            text = "Weekly plan\n" +
                                    "Role distribution",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.extended.surfaceHigher
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.at),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TaskyTimePicker(
                                selectedTime = DateTimeFormatter.formatTaskyDetailPickerTime(
                                    hour = state.localTimestamp.hour,
                                    minute = state.localTimestamp.minute,
                                ),
                                onValueChange = { hour, minute ->
                                    onAction(
                                        AgendaDetailAction.OnTimeFromPick(hour = hour, minute = minute)
                                    )
                                },
                                modifier = Modifier.requiredWidth(120.dp),
                                isReadOnly = isReadOnly
                            )
                            TaskyDatePicker(
                                selectedDate = DateTimeFormatter.formatTaskyDetailPickerDate(
                                    dateMillis = state.timestamp.toInstant().toEpochMilli()
                                ),
                                onValueChange = { dateMillis ->
                                    onAction(
                                        AgendaDetailAction.OnDateFromPick(dateMillis = dateMillis)
                                    )
                                },
                                modifier = Modifier.requiredWidth(156.dp),
                                isReadOnly = isReadOnly
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.extended.surfaceHigher
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TaskyAgendaItemDropdownMenu(
                            selectedReminder = state.selectedAgendaReminderInterval,
                            availableIntervals = defaultAgendaItemIntervals(),
                            onReminderSelected = {
                                onAction(
                                    AgendaDetailAction.OnAgendaItemIntervalSelect(reminder = it)
                                )
                            }
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.extended.surfaceHigher
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.extended.surfaceHigher
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TaskyTextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.delete_context,
                                    agendaItemTypeConfiguration.displayName.uppercase()
                                ),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AgendaDetailScreenPreview() {
    TaskyTheme {
        AgendaDetailScreen(
            state = AgendaDetailState(),
            onAction = {},
            appBarTitle = "Title",
            agendaDetailView = AgendaDetailView.EDIT,
            agendaItemTypeConfiguration = AgendaDetailConfigProvider.getConfig(type = AgendaItemType.TASK)
        )
    }
}
