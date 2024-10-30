package io.github.anicehome.webserver.service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.VerticalTwoPaneStrategy
import com.google.accompanist.adaptive.calculateDisplayFeatures

@Composable
actual fun ServiceScreenBox() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        PermissionBox(permission = Manifest.permission.POST_NOTIFICATIONS) {
            ServiceScreen()
        }
    } else {
        ServiceScreen()
    }
}

@Composable
actual fun TwoPanel(
    isPlaying: Boolean,
    serverUrls: List<String>,
    codes: List<ImageBitmap>,
    startServer: () -> Unit,
    stopServer: () -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val windowSizeClass = adaptiveInfo.windowSizeClass
    val activity = LocalContext.current as Activity
    val displayFeatures = calculateDisplayFeatures(activity)

    val usingHorizontalStrategy =
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM || windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
    if (usingHorizontalStrategy) {
        TwoPane(
            first = {
                if (isPlaying) {
                    FirstScreen(serverUrls, codes)
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        ServerStop()
                    }
                }
            },
            second = {
                SecondScreen(
                    isPlaying = isPlaying,
                    stopServer = stopServer,
                    startServer = startServer,
                )
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f),
            displayFeatures = displayFeatures
        )
    } else {
        TwoPane(
            first = {
                if (isPlaying) {
                    FirstScreen(serverUrls, codes)
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        ServerStop()
                    }
                }
            },
            second = {
                SecondScreen(
                    isPlaying = isPlaying,
                    stopServer = stopServer,
                    startServer = startServer,
                )
            },
            strategy = VerticalTwoPaneStrategy(splitFraction = 0.5f),
            displayFeatures = displayFeatures
        )
    }
}

actual class PlatformContext(val androidContext: Context)

@Composable
actual fun getPlatformContext(): PlatformContext = PlatformContext(LocalContext.current)

actual fun openWirelessSetting(platformContext: PlatformContext) {
    val activity = platformContext.androidContext as Activity
    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}

actual fun share(platformContext: PlatformContext, textShared: String) {
    val activity = platformContext.androidContext as Activity
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, textShared)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Address")
    activity.startActivity(shareIntent)
}