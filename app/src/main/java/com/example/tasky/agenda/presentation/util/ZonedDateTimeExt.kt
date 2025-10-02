@file:OptIn(ExperimentalTime::class)

package com.example.tasky.agenda.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant as KotlinInstant

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

fun ZonedDateTime.toUtc(): ZonedDateTime =
    withZoneSameInstant(ZoneId.of("UTC"))

fun ZonedDateTime.fromEpochMillis(
    epochMillis: Long,
    zoneId: ZoneId = ZoneId.of("UTC"),
): ZonedDateTime {
    return this
        .with(epochMillisToLocalDate(epochMillis))
        .withZoneSameInstant(zoneId)
}

fun ZonedDateTime.toKotlinInstant(): KotlinInstant {
    return KotlinInstant.fromEpochSeconds(this.toEpochSecond())
}

private fun epochMillisToLocalDate(epochMillis: Long): LocalDate {
    return Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
