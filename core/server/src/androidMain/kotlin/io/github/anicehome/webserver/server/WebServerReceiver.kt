package io.github.anicehome.webserver.server

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.also
import kotlin.apply
import kotlin.jvm.java

class WebServerReceiver constructor() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, WebServerService::class.java).apply {
            action = ACTION_STOP
        }.also { intent ->
            context?.startForegroundService(intent)
        }
    }
}