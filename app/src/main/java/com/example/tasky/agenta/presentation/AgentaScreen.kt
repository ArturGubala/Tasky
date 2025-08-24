@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenta.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyFloatingActionButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyProfileButtonMenu
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun AgendaScreenRoot() {
    AgendaScreen()
}

@Composable
private fun AgendaScreen() {
    var fabexpanded by remember { mutableStateOf(false) }
    var profileexpanded by remember { mutableStateOf(false) }


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
                            onClick = {
                                profileexpanded = !profileexpanded
                                      },
                            expanded = profileexpanded,
                            menuOptions = DefaultMenuOptions.getTaskyProfileMenuOptions()
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
                onClick = { fabexpanded = !fabexpanded },
                menuOptions = DefaultMenuOptions.getTaskyFabMenuOptions(
                    onEventClick = {
                        fabexpanded = false
                    },
                    onTaskClick = {
                        fabexpanded = false
                    },
                    onReminderClick = {
                        fabexpanded = false
                    }
                ),
                expanded = fabexpanded,
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
            ) {}
        }
    }
}

@PreviewLightDark
@Composable
private fun AgendaScreenPreview() {
    TaskyTheme {
        AgendaScreen()
    }
}
