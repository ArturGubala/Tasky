package com.example.tasky.agenda.di

import com.example.tasky.agenda.presentation.AgendaViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agendaViewModelModule = module {
    viewModelOf(::AgendaViewModel)
}
