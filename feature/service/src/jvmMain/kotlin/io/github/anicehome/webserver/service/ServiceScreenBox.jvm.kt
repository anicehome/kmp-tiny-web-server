package io.github.anicehome.webserver.service

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun ServiceScreenBox() {
    ServiceScreen()
}

@Composable
actual fun TwoPanel(
    isPlaying: Boolean,
    serverUrls: List<String>,
    codes: List<ImageBitmap>,
    startServer: () -> Unit,
    stopServer: () -> Unit,
) {
    Column {
        Box(modifier = Modifier.weight(1f)) {
            if (isPlaying) {
                FirstScreen(serverUrls, codes)
            } else {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    ServerStop()
                }
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            SecondScreen(
                isPlaying = isPlaying,
                stopServer = stopServer,
                startServer = startServer,
            )
        }
    }
}

actual class PlatformContext

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext()

actual fun openWirelessSetting(platformContext: PlatformContext) {

}

actual fun share(platformContext: PlatformContext, textShared: String) {
    // On Desktop share feature not supported
}