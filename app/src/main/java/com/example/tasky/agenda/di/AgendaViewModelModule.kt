package com.example.tasky.agenda.di

import com.example.tasky.agenda.presentation.agenda_list.AgendaViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agendaViewModelModule = module {
    viewModelOf(::AgendaViewModel)
}
