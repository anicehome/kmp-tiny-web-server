package io.github.anicehome.webserver.service.di

import io.github.anicehome.webserver.database.di.databaseModule
import io.github.anicehome.webserver.server.di.serverModule
import io.github.anicehome.webserver.service.ServiceViewModel
import io.github.anicehome.webserver.system.di.systemModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val serviceModule = module {
    includes(databaseModule, systemModule, serverModule)
    viewModelOf(::ServiceViewModel)
}