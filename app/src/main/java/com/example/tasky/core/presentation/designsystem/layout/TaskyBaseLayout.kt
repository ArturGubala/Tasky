package com.example.tasky.core.presentation.designsystem.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.core.presentation.designsystem.containers.TaskyContentBox
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun TaskyBaseLayout(
    content: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit) = {},
    floatingActionButton: @Composable (() -> Unit) = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        floatingActionButton = floatingActionButton,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { padding ->
        TaskyContentBox(
            content = content,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun TaskyBaseLayoutPreview() {
    TaskyTheme {
        TaskyBaseLayout(
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 28.dp)
                ) {
                    Text(
                        text = "TaskyContentBox",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = { Text("Top app bar") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        )
    }
}
