@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3ExpressiveApi::class)

package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
    Box(modifier = modifier
        .padding(bottom = 25.dp)) {
        AnimatedVisibility(
            visible = expanded,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(200)
            ) + fadeOut(animationSpec = tween(200)),
            modifier = Modifier
                .offset(x = 0.dp, y = (-80).dp)
        ) {
            Column(
                modifier = Modifier
                    .requiredWidth(160.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                menuOptions.forEach { menuOption ->
                    TaskyTextButton(
                        onClick = menuOption.onClick,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .animateContentSize()
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

        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(68.dp)
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (expanded) "Close menu" else "Open menu"
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyFloatingActionButtonMenuPreview() {
    TaskyTheme {
        Box(modifier = Modifier.padding(16.dp)) {
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
}
