package io.github.anicehome.webserver

import android.app.Application
import android.content.Context
import org.koin.dsl.module

class WebServerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            appModule = module {
                single<Context> { this@WebServerApplication }
            },
        )
    }
}