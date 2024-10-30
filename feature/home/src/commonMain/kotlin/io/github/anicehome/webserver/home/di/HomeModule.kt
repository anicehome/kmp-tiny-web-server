package io.github.anicehome.webserver.home.di

import io.github.anicehome.webserver.file.di.fileModule
import io.github.anicehome.webserver.service.di.serviceModule
import io.github.anicehome.webserver.setting.di.settingsModule
import org.koin.dsl.module

val homeModule = module {
    includes(
        fileModule,
        settingsModule,
        serviceModule
    )
}