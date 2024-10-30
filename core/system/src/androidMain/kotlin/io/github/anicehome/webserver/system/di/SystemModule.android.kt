package io.github.anicehome.webserver.system.di

import io.github.anicehome.webserver.system.AndroidSystemRepository
import io.github.anicehome.webserver.system.SystemRepository
import org.koin.dsl.module

internal actual val platformSystemModule = module {
    single<SystemRepository> { AndroidSystemRepository(get()) }
}