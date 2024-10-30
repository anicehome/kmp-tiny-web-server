package io.github.anicehome.webserver.database.di

import io.github.anicehome.webserver.database.jvmDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    single { jvmDatabaseBuilder() }
}