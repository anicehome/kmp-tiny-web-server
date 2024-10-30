package io.github.anicehome.webserver.server.di

import io.github.anicehome.webserver.server.JvmWebServer
import io.github.anicehome.webserver.server.WebServer
import org.koin.dsl.module

internal actual val platformServerModule = module {
    single<WebServer> { JvmWebServer() }
}