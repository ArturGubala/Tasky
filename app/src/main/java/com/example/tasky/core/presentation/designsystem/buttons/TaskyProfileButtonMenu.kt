package com.example.tasky.core.presentation.designsystem.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda.presentation.agenda_list.DefaultMenuOptions
import com.example.tasky.core.presentation.designsystem.TaskyProfileIcon
import com.example.tasky.core.presentation.designsystem.theme.TaskyTheme
import com.example.tasky.core.presentation.designsystem.theme.extended
import com.example.tasky.core.presentation.util.MenuOption

@Composable
fun TaskyProfileButtonMenu(
    text: String,
    onClick: () -> Unit,
    expanded: Boolean,
    menuOptions: List<MenuOption>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TaskyProfileIcon(
            text = text,
            modifier = Modifier
                .size(36.dp)
                .clickable { onClick.invoke() }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onClick,
            modifier = Modifier.requiredWidth(160.dp),
            offset = DpOffset(x = 0.dp, y = 7.dp),
            shape = RoundedCornerShape(8.dp),
            containerColor = MaterialTheme.colorScheme.extended.surfaceHigher
        ) {
            menuOptions.forEach { menuOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = menuOption.displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        menuOption.onClick()
                        onClick()
                    },
                    trailingIcon = {
                        menuOption.iconRes?.let {
                            if (!menuOption.enable) {
                                Icon(
                                    painter = painterResource(id = it),
                                    contentDescription = menuOption.contentDescription,
                                    modifier = Modifier.size(menuOption.iconSize),
                                )
                            }
                        }
                    },
                    enabled = menuOption.enable,
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70,
                        disabledTextColor = MaterialTheme.colorScheme.error.copy(alpha = .6f),
                        disabledLeadingIconColor = MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70
                    )
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TaskyProfileButtonMenuPreview() {
    TaskyTheme {
        var expanded by remember { mutableStateOf(true) }
        TaskyProfileButtonMenu(
            text = "AG",
            onClick = { expanded = !expanded },
            expanded = expanded,
            menuOptions = DefaultMenuOptions.getTaskyProfileMenuOptions()
        )
    }
}
