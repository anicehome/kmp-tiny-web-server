package io.github.anicehome.webserver.server

import io.github.anicehome.webserver.database.WebFileRepository
import io.github.anicehome.webserver.system.SystemRepository
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class JvmWebServer : WebServer {
    private lateinit var embeddedServer: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>
    private val serverRepository: ServerRepository by inject(ServerRepository::class.java)
    private val webFileRepository: WebFileRepository by inject(WebFileRepository::class.java)
    private val systemRepository: SystemRepository by inject(SystemRepository::class.java)

    override fun start() {
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

    override fun stop() {
        if (serverRepository.started.value) {
            embeddedServer.stop()
            serverRepository.updateStarted(false)
        }
    }
}