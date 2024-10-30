package io.github.anicehome.webserver.file.di

import io.github.anicehome.webserver.database.di.databaseModule
import io.github.anicehome.webserver.file.FileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val fileModule = module {
    includes(databaseModule)
    viewModelOf(::FileViewModel)
}