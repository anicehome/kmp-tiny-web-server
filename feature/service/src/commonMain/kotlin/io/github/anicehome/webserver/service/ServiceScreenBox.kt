package io.github.anicehome.webserver.service

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun ServiceScreenBox()

@Composable
expect fun TwoPanel(
    isPlaying: Boolean,
    serverUrls: List<String>,
    codes: List<ImageBitmap>,
    startServer: () -> Unit,
    stopServer: () -> Unit,
)

expect class PlatformContext

@Composable
expect fun getPlatformContext(): PlatformContext

expect fun openWirelessSetting(platformContext: PlatformContext)

expect fun share(platformContext: PlatformContext, textShared: String)