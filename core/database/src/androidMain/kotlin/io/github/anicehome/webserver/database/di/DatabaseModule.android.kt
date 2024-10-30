package io.github.anicehome.webserver.database.di

import io.github.anicehome.webserver.database.androidDatabaseBuilder
import org.koin.dsl.module

internal actual val platformDatabaseModule = module {
    single { androidDatabaseBuilder(get()) }
}