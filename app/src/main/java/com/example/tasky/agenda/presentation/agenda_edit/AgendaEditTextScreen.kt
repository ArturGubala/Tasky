package com.example.tasky.agenda.presentation.agenda_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tasky.R
import com.example.tasky.agenda.presentation.util.AgendaEditTextFieldType
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextFieldSecondary
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AgendaEditTextScreenRoot(
    fieldType: AgendaEditTextFieldType,
    text: String,
    onCancelClick: () -> Unit,
    onSaveClick: (text: String) -> Unit,
    viewModel: AgendaEditTextViewModel = koinViewModel(
        parameters = { parametersOf(text) }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            AgendaEditTextEvent.OnReadyAfterCancelClick -> {
                onCancelClick()
            }
            is AgendaEditTextEvent.OnReadyAfterSaveClick -> {
                onSaveClick(event.text)
            }
        }
    }

    AgendaEditTextScreen(
        state = state,
        onAction = viewModel::onAction,
        fieldType = fieldType,
        appBarTitle = fieldType.getScreenTitle(context = context)
    )
}

@Composable
fun AgendaEditTextScreen(
    state: AgendaEditTextState,
    onAction: (AgendaEditTextAction) -> Unit,
    fieldType: AgendaEditTextFieldType,
    appBarTitle: String
) {
    TaskyScaffold(
        topBar = {
            TaskyTopAppBar(
                leftActions = {
                    TaskyTextButton(
                        onClick = { onAction(AgendaEditTextAction.OnCancelClick) }
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                rightActions = {
                    TaskyTextButton(
                        onClick = {
                            onAction(
                                AgendaEditTextAction.OnSaveClick(
                                    text = state.textFieldState.text.toString()
                                )
                            )
                        }
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
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                title = {
                    Text(
                        text = appBarTitle.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            TaskyTextFieldSecondary(
                textFieldState = state.textFieldState,
                textStyle = when(fieldType) {
                    AgendaEditTextFieldType.TITLE -> {
                        MaterialTheme.typography.headlineLarge
                            .copy(color = MaterialTheme.colorScheme.primary)
                    }
                    AgendaEditTextFieldType.DESCRIPTION -> {
                        MaterialTheme.typography.bodyMedium
                            .copy(color = MaterialTheme.colorScheme.primary)
                    }
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                isSingleLine = fieldType.isSingleLine()
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun AgendaEditTextScreenPreview() {
    TaskyTheme {
        AgendaEditTextScreen(
            state = AgendaEditTextState(),
            onAction = {},
            appBarTitle = "EDIT TITLE",
            fieldType = AgendaEditTextFieldType.TITLE
        )
    }
}
