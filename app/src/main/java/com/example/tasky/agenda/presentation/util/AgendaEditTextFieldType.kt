package com.example.tasky.agenda.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import com.example.tasky.R

enum class AgendaEditTextFieldType(
    @param:StringRes val screenTitleRes: Int,
) {
    TITLE(R.string.edit_title),
    DESCRIPTION(R.string.edit_title);

    fun getScreenTitle(context: Context): String {
        val fieldType = when (this) {
            TITLE -> context.getString(R.string.title)
            DESCRIPTION -> context.getString(R.string.description)
        }
        return context.getString(screenTitleRes, fieldType)
    }

    fun isSingleLine(): Boolean {
        return when (this) {
            TITLE -> true
            DESCRIPTION -> false
        }
    }
}
