package com.example.tasky.core.data.di

import com.example.tasky.core.data.datastore.EncryptedSessionStorage
import com.example.tasky.core.data.networking.HttpClientFactory
import com.example.tasky.core.data.util.AndroidConnectivityObserver
import com.example.tasky.core.data.util.AndroidImageCompressor
import com.example.tasky.core.data.util.CryptoManager
import com.example.tasky.core.domain.datastore.SessionStorage
import com.example.tasky.core.domain.util.ConnectivityObserver
import com.example.tasky.core.domain.util.ImageCompressor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    single<CryptoManager>(named("session")) { CryptoManager("SessionEncryptionKey") }
    single<SessionStorage> {
        EncryptedSessionStorage(get(), get(named("session")))
    }
    singleOf(::AndroidConnectivityObserver).bind<ConnectivityObserver>()
    single<ImageCompressor> { AndroidImageCompressor(get()) }
    single<CoroutineDispatcher> { Dispatchers.Default }
}
