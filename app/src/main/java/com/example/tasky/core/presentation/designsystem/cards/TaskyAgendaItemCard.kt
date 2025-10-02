package com.example.tasky.core.presentation.designsystem.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.presentation.agenda_list.DefaultMenuOptions
import com.example.tasky.core.presentation.designsystem.drop_downs.TaskyAgendaListDropdownMenu
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.util.MenuOption

@Composable
fun TaskyAgendaItemCard(
    title: String,
    dates: String,
    menuOptions: List<MenuOption>,
    expanded: Boolean,
    onMenuClick: () -> Unit,
    onDismissRequest: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    isDone: Boolean? = null,
    onCompleteTaskClick: () -> Unit = {},
    description: String = "",
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isDone != null) {
                Column(
                    modifier = Modifier
                        .requiredWidth(30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                onCompleteTaskClick()
                            }
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Unchecked circle icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = "Unchecked circle icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textDecoration = isDone?.let {
                            if (isDone) {
                                TextDecoration.LineThrough
                            } else null
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Box(
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = "Unchecked circle icon",
                            modifier = Modifier
                                .clickable { onMenuClick() },
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        TaskyAgendaListDropdownMenu(
                            expanded = expanded,
                            options = menuOptions,
                            onDismissRequest = onDismissRequest
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = dates,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyAgendaItemCardPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TaskyAgendaItemCard(
                title = "Project X",
                dates = "Mar 3, 10:00 - Mar 5, 12:00",
                menuOptions = DefaultMenuOptions
                    .getTaskyAgendaItemMenuOptions(),
                expanded = false,
                onMenuClick = {},
                onDismissRequest = {},
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .requiredHeight(125.dp),
                description = "Description \n Sedasdasdasd\n"
            )
            TaskyAgendaItemCard(
                title = "Project X",
                dates = "Mar 3, 10:00",
                menuOptions = DefaultMenuOptions
                    .getTaskyAgendaItemMenuOptions(),
                expanded = false,
                onMenuClick = {},
                onDismissRequest = {},
                backgroundColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .requiredHeight(125.dp),
                isDone = true,
                description = "Description \n"
            )

            TaskyAgendaItemCard(
                title = "Project X",
                dates = "Mar 5, 12:00",
                menuOptions = DefaultMenuOptions
                    .getTaskyAgendaItemMenuOptions(),
                expanded = false,
                onMenuClick = {},
                onDismissRequest = {},
                backgroundColor = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                modifier = Modifier
                    .requiredHeight(125.dp),
                isDone = false,
                description = "Description \n"
            )
        }
    }
}
