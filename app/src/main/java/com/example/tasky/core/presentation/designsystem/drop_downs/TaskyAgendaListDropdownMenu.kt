package com.example.tasky.core.presentation.designsystem.drop_downs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.presentation.agenda_list.DefaultMenuOptions
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.util.MenuOption
import com.example.tasky.core.presentation.util.MenuOptionType

@Composable
fun TaskyAgendaListDropdownMenu(
    expanded: Boolean,
    menuOptions: List<MenuOption<MenuOptionType.AgendaItem>>,
    onMenuOptionClick: (MenuOption<MenuOptionType.AgendaItem>) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
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
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onDismissRequest.invoke() },
                modifier = Modifier.requiredWidth(120.dp),
                offset = DpOffset(
                    x = dropdownWidth - 120.dp,
                    y = 0.dp
                ),
                shape = RoundedCornerShape(8.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                menuOptions.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.displayName.asString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onMenuOptionClick(option)
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
}

@PreviewLightDark
@Composable
private fun TaskyAgendaListDropdownMenuPreview() {
    TaskyTheme {
        var expanded by remember { mutableStateOf(false) }
        TaskyAgendaListDropdownMenu(
            expanded = expanded,
            menuOptions = DefaultMenuOptions.getTaskyAgendaItemMenuOptions(),
            onMenuOptionClick = { option -> },
            onDismissRequest = { expanded = false }
        )
    }
}
