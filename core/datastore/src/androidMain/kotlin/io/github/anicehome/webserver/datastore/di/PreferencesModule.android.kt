package io.github.anicehome.webserver.datastore.di

import io.github.anicehome.webserver.datastore.androidDataStore
import org.koin.dsl.module

actual val preferencesPlatformModule = module {
    single { androidDataStore(get()) }
}
