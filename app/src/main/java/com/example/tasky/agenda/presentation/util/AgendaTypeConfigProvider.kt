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
    fun getConfig(type: AgendaType): AgendaTypeConfig {
        return when (type) {
            AgendaType.TASK -> createTaskConfig()
            AgendaType.EVENT -> createEventConfig()
            AgendaType.REMINDER -> createReminderConfig()
        }
    }

    private fun createTaskConfig() = AgendaTypeConfig(
        type = AgendaType.TASK,
        displayName = "Task",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.secondary }
    )

    private fun createEventConfig() = AgendaTypeConfig(
        type = AgendaType.EVENT,
        displayName = "Event",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.tertiary }
    )

    private fun createReminderConfig() = AgendaTypeConfig(
        type = AgendaType.REMINDER,
        displayName = "Reminder",
        editTitleTemplateRes = R.string.edit_title,
        colorProvider = { MaterialTheme.colorScheme.extended.surfaceHigher },
        strokeColorProvider = { MaterialTheme.colorScheme.extended.onSurfaceVariantOpacity70 }
    )
}

data class AgendaTypeConfig(
    val type: AgendaType,
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
        mode: AgendaMode,
        context: Context,
        itemDate: LocalDateTime? = null
    ): String {
        return when (mode) {
            AgendaMode.VIEW -> {
                itemDate?.let { date ->
                    formatDateForTitle(date, context)
                } ?: context.getString(R.string.no_date)
            }
            AgendaMode.EDIT -> context.getString(editTitleTemplateRes, displayName)
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
