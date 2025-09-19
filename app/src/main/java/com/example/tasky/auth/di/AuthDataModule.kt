package com.example.tasky.auth.di

import com.example.tasky.auth.data.AuthRepositoryImpl
import com.example.tasky.core.domain.util.EmailPatternValidator
import com.example.tasky.auth.domain.AuthRepository
import com.example.tasky.core.domain.PatternValidator
import com.example.tasky.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}
