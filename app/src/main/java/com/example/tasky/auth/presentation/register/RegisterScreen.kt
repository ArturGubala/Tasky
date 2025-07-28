package com.example.tasky.auth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.buttons.TaskyLink
import com.example.tasky.core.presentation.designsystem.buttons.TaskyPrimaryButton
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyPasswordTextField
import com.example.tasky.core.presentation.designsystem.text_fields.TaskyTextField
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onLogInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterScreen() {
    TaskyScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_your_account),
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .padding(start = 40.dp, top = 40.dp, end = 40.dp, bottom = 36.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 28.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TaskyTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        hintText = stringResource(R.string.name)
                    )
                    TaskyTextField(
                        text = "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        hintText = stringResource(R.string.email_address)
                    )
                    TaskyPasswordTextField(
                        state = TextFieldState(""),
                        isPasswordVisible = false,
                        onTogglePasswordVisibility = {},
                        hintText = stringResource(R.string.password),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    TaskyPrimaryButton(
                        content = {
                            Text(
                                text = stringResource(R.string.get_started),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(R.string.already_have_an_account),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall
                        )
                        TaskyLink(
                            onClick = {},
                            content = {
                                Text(
                                    text = stringResource(R.string.log_in),
                                    color = MaterialTheme.colorScheme.extended.link,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun RegisterScreenPreview() {
    TaskyTheme {
        RegisterScreen()
    }
}
