package com.example.tasky.core.presentation.designsystem.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.TaskyProfileIcon
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.designsystem.theme.headlineXSmall
import com.example.tasky.core.presentation.designsystem.theme.labelXSmall

@Composable
fun TaskyAttendeeCard(
    attendeeName: String,
    isCreator: Boolean,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    canEdit: Boolean = false
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.extended.surfaceHigher)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskyProfileIcon(
            text = "AG",
            textColor = MaterialTheme.colorScheme.onBackground,
            textStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
            backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(32.dp)
        )
        Text(
            text = attendeeName,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineXSmall
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isCreator) {
            Text(
                text = stringResource(R.string.creator),
                color = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                style = MaterialTheme.typography.labelXSmall
            )
        } else if (canEdit) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bin),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TaskyAttendeeCardPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TaskyAttendeeCard(
                attendeeName = "Artur Gubala",
                isCreator = true
            )
            TaskyAttendeeCard(
                attendeeName = "Artur Gubala",
                isCreator = false,
                onDeleteClick = {}
            )
        }
    }
}
