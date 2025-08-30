package com.example.tasky.agenda.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tasky.R
import com.example.tasky.core.presentation.designsystem.theme.extended
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

object AgendaTypeConfigProvider {
    fun getConfig(type: AgendaItemType): AgendaTypeConfig {
        return when (type) {
            AgendaItemType.TASK -> createTaskConfig()
            AgendaItemType.EVENT -> createEventConfig()
            AgendaItemType.REMINDER -> createReminderConfig()
        }
    }

    private fun createTaskConfig() = AgendaTypeConfig(
        type = AgendaItemType.TASK,
        displayName = "Task",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.secondary }
    )

    private fun createEventConfig() = AgendaTypeConfig(
        type = AgendaItemType.EVENT,
        displayName = "Event",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.tertiary }
    )

    private fun createReminderConfig() = AgendaTypeConfig(
        type = AgendaItemType.REMINDER,
        displayName = "Reminder",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.extended.surfaceHigher },
        strokeColorProvider = { MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70 }
    )
}

data class AgendaTypeConfig(
    val type: AgendaItemType,
    val displayName: String,
    @param:StringRes val editTitleTemplateRes: Int,
    private val colorProvider: @Composable () -> Color,
    private val strokeColorProvider: (@Composable () -> Color)? = null
) {
    @Composable
    fun getColor(): Color = colorProvider()

    @Composable
    fun getStrokeColor(): Color? = strokeColorProvider?.invoke()

    fun getAppBarTitle(
        mode: AgendaDetailView,
        context: Context,
        itemDate: LocalDateTime? = null
    ): String {
        return when (mode) {
            AgendaDetailView.READ_ONLY -> {
                itemDate?.let { date ->
                    formatDateForTitle(date, context)
                } ?: context.getString(R.string.no_date)
            }
            AgendaDetailView.EDIT -> context.getString(editTitleTemplateRes, displayName)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatDateForTitle(date: LocalDateTime, context: Context): String {
        val day = String.format("%02d", date.dayOfMonth)
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val year = date.year
        return "$day $month $year"
    }
}
