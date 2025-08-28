@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.tasky.agenda.presentation.agenda_list.DefaultMenuOptions
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.util.MenuOption

@Composable
fun TaskyFloatingActionButtonMenu(
    onClick: () -> Unit,
    expanded: Boolean,
    menuOptions: List<MenuOption>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(68.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (expanded) "Close menu" else "Open menu"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onClick,
            modifier = Modifier.requiredWidth(160.dp),
            offset = DpOffset(
                x = 0.dp,
                y = -(68.dp + 8.dp + (menuOptions.size * 56.dp)) // FAB height + spacing + menu items height
            ),
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            menuOptions.forEach { menuOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = menuOption.displayName,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        menuOption.onClick()
                        onClick()
                    },
                    leadingIcon = {
                        menuOption.iconRes?.let {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = menuOption.contentDescription,
                                modifier = Modifier.size(menuOption.iconSize),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary,
                        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyFloatingActionButtonMenuPreview() {
    TaskyTheme {
        var expanded by remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            TaskyFloatingActionButtonMenu(
                onClick = { expanded = !expanded },
                expanded = expanded,
                menuOptions = DefaultMenuOptions.getTaskyFabMenuOptions(
                    onEventClick = { },
                    onTaskClick = { },
                    onReminderClick = { }
                )
            )
        }
    }
}
