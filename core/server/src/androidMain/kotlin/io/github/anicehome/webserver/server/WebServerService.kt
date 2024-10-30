package io.github.anicehome.webserver.server

import android.app.Notification
import android.os.IBinder
import androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID
import androidx.core.content.getSystemService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.anicehome.webserver.database.WebFileRepository
import io.github.anicehome.webserver.system.SystemRepository
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import org.koin.android.ext.android.inject
import kotlin.apply
import kotlin.jvm.java
import kotlin.run

const val ACTION_START = "start"
const val ACTION_STOP = "stop"
const val ONGOING_NOTIFICATION_ID = 100
const val CHANNEL_DEFAULT_IMPORTANCE = "channel"
const val ACTION_EXIT = "io.github.anicehome.webserver.Action.Exit"

class WebServerService : Service() {
    private lateinit var embeddedServer: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>

    private val serverRepository: ServerRepository by inject()
    private val webFileRepository: WebFileRepository by inject()
    private val systemRepository: SystemRepository by inject()

    private var startMode: Int = 0             // indicates how to behave if the service is killed
    private var binder: IBinder? = null        // interface for clients that bind
    private var allowRebind: Boolean = false   // indicates whether onRebind should be used

    override fun onCreate() {
        super.onCreate()//没有这句话居然会导致hilt注入不了
        // The service is being created
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(NOTIFICATION_ID, createNotification())

        when (intent?.action) {
            ACTION_START -> {
                startServer()
            }

            ACTION_STOP -> {
                stopServer()
                stopSelf()
            }

        }

        // The service is starting, due to a call to startService()
        return startMode
    }

    private fun restartServer() {
        stopServer()
        startServer()
    }

    private fun stopServer() {
        if (serverRepository.started.value) {
            embeddedServer.stop()
            serverRepository.updateStarted(false)
        }
    }

    private fun startServer() {
        if (serverRepository.started.value) {
            return
        }
        val port: Int = (30000..40000).random()
        serverRepository.updatePort(port)
        embeddedServer = embeddedServer(factory = CIO, port = port, host = "0.0.0.0") {
            configureRouting(
                webFileRepository = webFileRepository,
                systemRepository = systemRepository,
            )
        }
        embeddedServer.start(false)
        serverRepository.updateStarted(true)
    }

    companion object {

        private const val NOTIFICATION_ID = 123
        private const val CHANNEL_ID = "demo_session_notification_channel_id"
    }

    private fun createNotification(): Notification {
        val exitIntent = Intent(this, WebServerReceiver::class.java).apply {
            action = ACTION_EXIT
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val exitPendingIntent: PendingIntent =
            getBroadcast(this, 10, exitIntent, FLAG_IMMUTABLE)

        val notificationManagerCompat = NotificationManagerCompat.from(this@WebServerService)
        ensureNotificationChannel(notificationManagerCompat)
        val pendingIntent =
            TaskStackBuilder.create(this@WebServerService).run {
                val clazz = Class.forName("io.github.anicehome.webserver.MainActivity")
                addNextIntent(Intent(this@WebServerService, clazz))
                getPendingIntent(0, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
            }
        val builder =
            NotificationCompat.Builder(this@WebServerService, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.baseline_connect_without_contact_24)
                .setContentTitle(getString(R.string.notification_title))
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_message))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.baseline_exit_to_app_24, "Exit", exitPendingIntent)
                .setAutoCancel(true)
//        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build())
        return builder.build()
    }


    private fun ensureNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        notificationManagerCompat.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent): IBinder? {
        // A client is binding to the service with bindService()
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // All clients have unbound with unbindService()
        return allowRebind
    }

    override fun onRebind(intent: Intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    override fun onDestroy() {
        // The service is no longer used and is being destroyed
        getSystemService<NotificationManager>()?.cancel(ONGOING_NOTIFICATION_ID)
    }

}