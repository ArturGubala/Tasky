package com.example.tasky.core.presentation.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
object DateTimeFormatter {

    fun formatTaskyDetailPickerTime(hour: Int, minute: Int): String {
        return String.format(DateTimeFormats.TASKY_DETAIL_PICKER_TIME, hour, minute)
    }

    @SuppressLint("ConstantLocale")
    private val dateFormat = SimpleDateFormat(DateTimeFormats.TASKY_DETAIL_PICKER_DATE, Locale.getDefault())

    fun formatTaskyDetailPickerDate(dateMillis: Long): String {
        return dateFormat.format(Date(dateMillis))
    }
}

object DateTimeFormats {
    const val TASKY_DETAIL_PICKER_TIME = "%02d:%02d"
    const val TASKY_DETAIL_PICKER_DATE = "MMM dd, yyyy"
}
