package com.example.tasky.agenda.presentation.util

fun String.toInitials(): String {
    val trimmedName = this.trim()
    if (trimmedName.isEmpty()) return ""

    val nameParts = trimmedName.split(" ").filter { it.isNotBlank() }

    return when (nameParts.size) {
        0 -> ""
        1 -> {
            nameParts[0].take(2).uppercase()
        }
        else -> {
            "${nameParts.first().first()}${nameParts.last().first()}".uppercase()
        }
    }
}
