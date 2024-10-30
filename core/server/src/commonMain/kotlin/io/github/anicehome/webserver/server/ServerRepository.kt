package io.github.anicehome.webserver.server

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ServerRepository {
    private val _started = MutableStateFlow(false)
    private val _port = MutableStateFlow(0)

    val started: StateFlow<Boolean>
        get() = _started

    val port: StateFlow<Int>
        get() = _port

    fun updateStarted(started: Boolean) {
        _started.update { started }
    }

    fun updatePort(port: Int) {
        _port.update { port }
    }
}