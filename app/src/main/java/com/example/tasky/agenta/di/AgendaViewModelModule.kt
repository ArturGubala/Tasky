package com.example.tasky.agenta.di

import com.example.tasky.agenta.presentation.AgendaViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val agendaViewModelModule = module {
    viewModelOf(::AgendaViewModel)
}
