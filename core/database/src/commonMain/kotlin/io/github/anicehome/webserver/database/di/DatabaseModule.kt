package io.github.anicehome.webserver.database.di

import io.github.anicehome.webserver.database.WebFileRepository
import io.github.anicehome.webserver.database.WebServerDatabase
import io.github.anicehome.webserver.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule = module {
    includes(platformDatabaseModule)

    single { getRoomDatabase(get()) }
    single {
        WebFileRepository(
            get<WebServerDatabase>().webFileDao(),
            get<WebServerDatabase>().transactionRunnerDao()
        )
    }
}

internal expect val platformDatabaseModule: Module
