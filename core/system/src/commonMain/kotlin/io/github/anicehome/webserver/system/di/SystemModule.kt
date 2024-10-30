package io.github.anicehome.webserver.system.di

import org.koin.core.module.Module
import org.koin.dsl.module

val systemModule = module {
    includes(platformSystemModule)

}

internal expect val platformSystemModule: Module