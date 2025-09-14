package com.example.tasky.agenda.presentation.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import com.example.tasky.R

@SuppressLint("SupportAnnotationUsage")
enum class AgendaItemAttendeesStatus(
    @param:StringRes val displayNameRes: Int,
) {
    ALL(R.string.all),
    GOING(R.string.going),
    NOT_GOING(R.string.not_going);

    fun getDisplayName(context: Context): String {
        return context.getString(displayNameRes)
    }
}
