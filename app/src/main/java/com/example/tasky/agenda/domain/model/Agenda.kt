package com.example.tasky.agenda.domain.model

data class Agenda(
    val tasks: List<Task>,
    val events: List<Event>,
    val reminders: List<Reminder>,
)
