package io.github.anicehome.webserver.datastore.di

import io.github.anicehome.webserver.datastore.jvmDataStore
import org.koin.dsl.module

actual val preferencesPlatformModule = module {
    single { jvmDataStore() }
}
