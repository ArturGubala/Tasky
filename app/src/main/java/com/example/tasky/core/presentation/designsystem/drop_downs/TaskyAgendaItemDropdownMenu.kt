@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.core.presentation.designsystem.drop_downs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.presentation.util.AgendaItemInterval
import com.example.tasky.agenda.presentation.util.defaultAgendaItemIntervals
import com.example.tasky.agenda.presentation.util.toUiText
import com.example.tasky.core.presentation.designsystem.icons.TaskySquare
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyAgendaItemDropdownMenu(
    selectedReminder: AgendaItemInterval,
    availableIntervals: List<AgendaItemInterval>,
    onReminderSelected: (AgendaItemInterval) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    var dropdownWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) {
                if (enabled) expanded = !expanded
            }
            .onSizeChanged { size ->
                dropdownWidth = with(density) { size.width.toDp() }
            },
        contentAlignment = Alignment.TopEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TaskySquare(
                size = 32.dp,
                color = MaterialTheme.colorScheme.extended.surfaceHigher
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications icon",
                    tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                )
            }
            Text(
                text = selectedReminder.toUiText().asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Dropdown arrow",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(if (expanded) 180f else 0f),
                tint = if (enabled) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                }
            )
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.requiredWidth(240.dp),
            offset = DpOffset(
                x = dropdownWidth - 240.dp,
                y = 0.dp
            ),
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            availableIntervals.forEach { interval ->
                val isOptionSelected = interval == selectedReminder
                DropdownMenuItem(
                    text = {
                        Text(
                            text = interval.toUiText().asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onReminderSelected(interval)
                        expanded = false
                    },
                    modifier = if (isOptionSelected) {
                        Modifier.background(MaterialTheme.colorScheme.extended.surfaceHigher)
                    } else {
                        Modifier
                    },
                    trailingIcon = {
                        if (isOptionSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.extended.success
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.primary
                    ),
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun NotificationReminderDropdownPreview() {
    TaskyTheme {
        var selectedReminder by remember {
            mutableStateOf<AgendaItemInterval>(AgendaItemInterval.TenMinutesFromNow)
        }

        Text(
            text = "Notification Reminder Selection",
            style = MaterialTheme.typography.headlineSmall
        )

        TaskyAgendaItemDropdownMenu(
            selectedReminder = selectedReminder,
            availableIntervals = defaultAgendaItemIntervals(),
            onReminderSelected = { reminder ->
                selectedReminder = reminder
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
