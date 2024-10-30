package io.github.anicehome.webserver.server

import android.content.Context
import android.content.Intent

class AndroidWebServer constructor(
    private val context: Context,
) : WebServer {
    override fun start() {
        Intent(context, WebServerService::class.java).apply {
            action = ACTION_START
        }.also { intent ->
            context.startForegroundService(intent)
        }
    }

    override fun stop() {
        Intent(context, WebServerService::class.java).apply {
            action = ACTION_STOP
        }.also { intent ->
            context.startForegroundService(intent)
        }
    }
}