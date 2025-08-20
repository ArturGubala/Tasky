@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.agenta.presentation.DefaultMenuOptions
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.util.MenuOption

@Composable
fun TaskyFloatingActionButtonMenu(
    onClick: () -> Unit,
    expanded: Boolean,
    menuOptions: List<MenuOption>,
    modifier: Modifier = Modifier
) {
    FloatingActionButtonMenu(
        expanded = expanded,
        button = {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(68.dp),
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add icon"
                )
            }
        },
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .requiredWidth(160.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            menuOptions.forEach { menuOption ->
                TaskyTextButton(
                    onClick = menuOption.onClick,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = menuOption.iconRes),
                            contentDescription = menuOption.contentDescription,
                            modifier = Modifier.size(menuOption.size),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = menuOption.displayName,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyFloatingActionButtonMenuPreview() {
    TaskyTheme {
        TaskyFloatingActionButtonMenu(
            onClick = {},
            expanded = true,
            menuOptions = DefaultMenuOptions.getTaskyMenuOptions(
                onEventClick = { },
                onTaskClick = { },
                onReminderClick = { }
            )
        )
    }
}
