package io.github.anicehome.webserver.datastore.di

import io.github.anicehome.webserver.datastore.PreferencesDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

val preferencesModule = module {
    includes(preferencesPlatformModule)

    single { PreferencesDataSource(get()) }
}

expect val preferencesPlatformModule: Module
