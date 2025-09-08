package com.example.tasky.core.presentation.util

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
object DateTimeFormatter {

    fun formatTaskyDetailPickerTime(hour: Int, minute: Int): String {
        return String.format(DateTimeFormats.TASKY_DETAIL_PICKER_TIME, hour, minute)
    }

    @SuppressLint("ConstantLocale")
    private val taskyDetailPickerDateFormat =
        DateTimeFormatter.ofPattern(DateTimeFormats.TASKY_DETAIL_PICKER_DATE)
    fun formatTaskyDetailPickerDate(dateMillis: Long): String {
        return taskyDetailPickerDateFormat.format(ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(dateMillis),
            ZoneId.systemDefault())
        )
    }

    @SuppressLint("ConstantLocale")
    private val taskyDetailTitleDateFormat =
        DateTimeFormatter.ofPattern(DateTimeFormats.TASKY_DETAIL_TITLE_DATE)
    fun formatTaskyDetailTitleDate(dateMillis: Long): String {
        return taskyDetailTitleDateFormat.format(ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(dateMillis),
            ZoneId.systemDefault())
        )
    }
}

object DateTimeFormats {
    const val TASKY_DETAIL_PICKER_TIME = "%02d:%02d"
    const val TASKY_DETAIL_PICKER_DATE = "MMM dd, yyyy"
    const val TASKY_DETAIL_TITLE_DATE = "dd MMMM yyyy"
}
