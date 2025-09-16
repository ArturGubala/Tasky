package com.example.tasky.core.presentation.designsystem.pickers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.tasky.R
import com.example.tasky.agenda.domain.model.Photo
import com.example.tasky.agenda.domain.util.MAX_NUMBER_OF_PHOTOS
import com.example.tasky.core.presentation.designsystem.buttons.TaskyTextButton
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended

@Composable
fun TaskyPhotoPicker(
    photos: List<Photo>,
    modifier: Modifier = Modifier,
    onPhotoClick: (Photo) -> Unit = {},
    onAddClick: () -> Unit = {},
    isReadOnly: Boolean = false,
) {
    val thumbnailSize = 64.dp

    if (photos.isNotEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Photos",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 5
            ) {
                photos.forEach { photo ->
                    PhotoItem(
                        photo = photo,
                        onPhotoClick = { onPhotoClick(photo) },
                        modifier = Modifier.size(thumbnailSize)
                    )
                }

                if (!isReadOnly && photos.size < MAX_NUMBER_OF_PHOTOS) {
                    AddPhotoThumbnail(
                        onClick = onAddClick,
                        modifier = Modifier.size(thumbnailSize)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            TaskyTextButton(
                onClick = onAddClick
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_icon),
                        tint = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                    )
                    Text(
                        text = stringResource(R.string.add_photos),
                        color = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}


@Composable
fun PhotoItem(
    photo: Photo,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                2.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(5.dp)
            )
            .clip(RoundedCornerShape(5.dp))
            .clickable { onPhotoClick() }
    ) {
        SubcomposeAsyncImage(
            model = photo.uri,
            contentDescription = "Selected photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = stringResource(R.string.error_loading_image),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}

@Composable
fun AddPhotoThumbnail(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(Color.Transparent)
            .border(
                2.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(5.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_photo),
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(24.dp)
        )
    }
}

@PreviewLightDark
@Composable
fun TaskyPhotoPickerPreview() {
    TaskyTheme {
        TaskyPhotoPicker(
            photos = emptyList()
        )
    }
}
