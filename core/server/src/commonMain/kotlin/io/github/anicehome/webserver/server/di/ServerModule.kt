package io.github.anicehome.webserver.server.di

import io.github.anicehome.webserver.server.ServerRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val serverModule = module {
    includes(platformServerModule)

    single { ServerRepository() }
}

internal expect val platformServerModule: Module