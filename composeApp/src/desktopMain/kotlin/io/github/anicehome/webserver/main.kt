package io.github.anicehome.webserver

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.dsl.module

fun main() = application {
    initKoin(module {})
    val state = rememberWindowState(
        size = DpSize(1280.dp, 720.dp),
    )
    Window(
        state = state,
        onCloseRequest = ::exitApplication,
        title = "kmp-tiny-web-server",
    ) {
        App()
    }
}