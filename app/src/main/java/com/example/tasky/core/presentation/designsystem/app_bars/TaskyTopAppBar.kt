@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.core.presentation.designsystem.app_bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyTopAppBar(
    leftActions: @Composable (() -> Unit),
    rightActions: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit) = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.background,
        navigationIconContentColor = MaterialTheme.colorScheme.background,
        actionIconContentColor = MaterialTheme.colorScheme.background,
    )
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = leftActions,
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) { rightActions.invoke() }
        },
        colors = colors
    )
}

@PreviewLightDark
@Composable
private fun TaskyTopAppBarPreview() {
    val isCalendarExpanded = false
    val icon: ImageVector =
        if (isCalendarExpanded) Icons.Filled.ArrowDropDown
        else Icons.AutoMirrored.Outlined.ArrowRight


    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TaskyTopAppBar(
                leftActions = {
                    TaskyTextButton(
                        onClick = {}
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MARCH",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Icon(
                                imageVector = icon,
                                contentDescription = "Arrow right icon",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                rightActions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar_today),
                            contentDescription = "Arrow right icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Arrow right icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                title = {
                    Text(
                        text = "Edit task",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
