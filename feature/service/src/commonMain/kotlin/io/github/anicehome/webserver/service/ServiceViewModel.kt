package io.github.anicehome.webserver.service

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.anicehome.webserver.server.ServerRepository
import io.github.anicehome.webserver.server.WebServer
import io.github.anicehome.webserver.system.SystemRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ServiceViewModel constructor(
    private val systemRepository: SystemRepository,
    private val serverRepository: ServerRepository,
    private val webServer: WebServer,
) :
    ViewModel() {
    val serverUrls: StateFlow<List<String>> =
        systemRepository.network().combine(serverRepository.port) { addresses, port ->
            addresses.map {
                "http://${it}:$port"
            }
//                .filter { linkAddress -> linkAddress.address.isSiteLocalAddress }
//                .map {
//                    when (val address = it.address) {
//                        is Inet4Address -> "http://${address.hostAddress}:$port"
//                        is Inet6Address -> "http://[${address.hostAddress}]:$port"
//                        else -> { "" }
//                    }
//                }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = emptyList(),
        )

    val qrCodeImageBitmap: StateFlow<List<ImageBitmap>> = serverUrls.map {
        val codes = mutableListOf<ImageBitmap>()
        it.forEach {
            val url = it
            val bitmap = createImageBitmap(url, 192)
            codes.add(bitmap)
        }
        codes
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = emptyList()
    )

    val started = serverRepository.started

    fun startServer() {
        viewModelScope.launch {
            webServer.start()
        }
    }

    fun stopServer() {
        viewModelScope.launch {
            webServer.stop()
        }
    }

}