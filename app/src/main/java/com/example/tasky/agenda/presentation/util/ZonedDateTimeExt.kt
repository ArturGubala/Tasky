package com.example.tasky.agenda.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.updateUtcTime(hour: Int, minutes: Int): ZonedDateTime {
    return this
        .withZoneSameInstant(ZoneId.systemDefault())
        .withHour(hour)
        .withMinute(minutes)
        .withZoneSameInstant(ZoneId.of("UTC"))
}

fun ZonedDateTime.updateUtcDate(dateMillis: Long): ZonedDateTime {
    return this
        .withZoneSameInstant(ZoneId.systemDefault())
        .with(epochMillisToLocalDate(dateMillis))
        .withZoneSameInstant(ZoneId.of("UTC"))
}

fun ZonedDateTime.toLocal(): ZonedDateTime =
    withZoneSameInstant(ZoneId.systemDefault())

private fun epochMillisToLocalDate(epochMillis: Long): LocalDate {
    return Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
