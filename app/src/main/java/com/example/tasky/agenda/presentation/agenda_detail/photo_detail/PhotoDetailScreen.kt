@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tasky.agenda.presentation.agenda_detail.photo_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.tasky.R
import com.example.tasky.agenda.presentation.util.AgendaDetailView
import com.example.tasky.core.presentation.designsystem.app_bars.TaskyTopAppBar
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.layout.TaskyScaffold
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme

@Composable
fun PhotoDetailScreenRoot(
    imageUri: String,
    agendaDetailView: AgendaDetailView,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    PhotoDetailScreen(
        imageUri = imageUri,
        agendaDetailView = agendaDetailView,
        onCloseClick = onCloseClick,
        onDeleteClick = onDeleteClick
    )
}

@Composable
fun PhotoDetailScreen(
    imageUri: String,
    agendaDetailView: AgendaDetailView,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val context = LocalContext.current

    TaskyScaffold(
        topBar = {
            TaskyTopAppBar(
                leftActions = {
                    TaskyTextButton(
                        onClick = { onCloseClick() }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                rightActions = {
                    if (agendaDetailView == AgendaDetailView.EDIT) {
                        TaskyTextButton(
                            onClick = {
                                onDeleteClick()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_bin),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                title = {
                    Text(
                        text = "PHOTO",
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
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUri.toUri())
                    .crossfade(true)
                    .build(),
                contentDescription = "Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentScale = ContentScale.Fit,
                loading = {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                },
                error = {
                    Text(
                        text = "Can't load image",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PhotoDetailScreenPreview() {
    TaskyTheme {
        PhotoDetailScreen(
            imageUri = "content://media/picker/0/com.android.providers.media.photopicker/media/1000000380",
            agendaDetailView = AgendaDetailView.EDIT,
            onCloseClick = {},
            onDeleteClick = {}
        )
    }
}
