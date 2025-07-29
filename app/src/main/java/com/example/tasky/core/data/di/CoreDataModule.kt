package com.example.tasky.core.data.di

import com.example.tasky.core.data.datastore.EncryptedSessionStorage
import com.example.tasky.core.data.util.CryptoManager
import com.example.tasky.core.domain.datastore.SessionStorage
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreDataModule = module {
    single<CryptoManager>(named("session")) { CryptoManager("SessionEncryptionKey") }
    single<SessionStorage> {
        EncryptedSessionStorage(get(), get(named("session")))
    }
}
