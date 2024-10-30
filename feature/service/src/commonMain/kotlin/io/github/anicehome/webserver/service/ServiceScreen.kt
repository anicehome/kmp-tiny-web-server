package io.github.anicehome.webserver.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.anicehome.webserver.resource.Res
import io.github.anicehome.webserver.resource.cd_pause
import io.github.anicehome.webserver.resource.cd_play
import io.github.anicehome.webserver.resource.connection_message
import io.github.anicehome.webserver.resource.connection_title
import io.github.anicehome.webserver.resource.copy_base_url
import io.github.anicehome.webserver.resource.notification_title
import io.github.anicehome.webserver.resource.server_title
import io.github.anicehome.webserver.resource.share_base_ur
import io.github.anicehome.webserver.resource.start_server_message
import io.github.anicehome.webserver.resource.wireless_settings
import io.github.anicehome.webserver.resource.WebServerIcons
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ServiceScreen(
    serviceViewModel: ServiceViewModel = koinViewModel(),
) {

    val serverUrls by serviceViewModel.serverUrls.collectAsStateWithLifecycle()
    val codes by serviceViewModel.qrCodeImageBitmap.collectAsStateWithLifecycle()

    val isPlaying by serviceViewModel.started.collectAsStateWithLifecycle()

    val startServer:() -> Unit = serviceViewModel::startServer
    val stopServer:() -> Unit = serviceViewModel::stopServer

    TwoPanel(isPlaying, serverUrls, codes, startServer, stopServer)
}

@Composable
fun FirstScreen(
    serverUrls: List<String>,
    codes: List<ImageBitmap>,
) {
    if (serverUrls.isEmpty()) {
        EmptyServer()
    } else {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val pageCount = serverUrls.size
                val pagerState = rememberPagerState {
                    // provide pageCount
                    pageCount
                }
                HorizontalPager(
                    state = pagerState,
                ) { page ->

                    val url = serverUrls[page]

                    val imageBitmap = codes[page]
                    UrlItem(url, imageBitmap)
                }
                Row(
                    Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(16.dp)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyServer() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                imageVector = WebServerIcons.Connection,
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(maxWidth = 96.dp, maxHeight = 96.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            )
            Text(
                text = stringResource(Res.string.connection_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(Res.string.connection_message),
                textAlign = TextAlign.Center,
            )
            val playerButtonSize: Dp = 48.dp
            val primaryButtonModifier = Modifier
                .size(playerButtonSize)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                )
                .semantics { role = Role.Button }
            val platformContext = getPlatformContext()
            Image(
                imageVector = WebServerIcons.Settings,
                contentDescription = stringResource(Res.string.wireless_settings),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = primaryButtonModifier
                    .padding(8.dp)
                    .clickable {
                        openWirelessSetting(platformContext)
                    }
            )
        }
    }
}

@Composable
fun SecondScreen(
    isPlaying: Boolean,
    stopServer: () -> Unit,
    startServer: () -> Unit,
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {


        val playerButtonSize: Dp = 72.dp
        val primaryButtonModifier = Modifier
            .size(playerButtonSize)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            )
            .semantics { role = Role.Button }


        if (isPlaying) {
            Image(
                imageVector = WebServerIcons.Stop,
                contentDescription = stringResource(Res.string.cd_pause),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = primaryButtonModifier
                    .padding(8.dp)
                    .clickable {
                        stopServer()
                    }
            )
        } else {
            Image(
                imageVector = WebServerIcons.PlayArrow,
                contentDescription = stringResource(Res.string.cd_play),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                modifier = primaryButtonModifier
                    .padding(8.dp)
                    .clickable {
                        startServer()
                    }
            )
        }
    }
}

@Composable
fun UrlItem(url: String, imageBitmap: ImageBitmap) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        QRCode(imageBitmap)
        AddressItem(url)
    }
}

@Composable
fun QRCode(
    imageBitmap: ImageBitmap
) {
    Image(
        bitmap = imageBitmap,
        contentDescription = stringResource(Res.string.notification_title),
        contentScale = ContentScale.Crop,
        modifier = Modifier.wrapContentSize()
    )
}

@Preview
@Composable
fun QRCodeImagePreview() {
    MaterialTheme {
        QRCode(imageBitmap = ImageBitmap(0, 0))
    }
}


@Composable
fun AddressItem(url: String) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = url, maxLines = 1, overflow = TextOverflow.Ellipsis)
        val localClipboardManager = LocalClipboardManager.current
        IconButton(onClick = {
            clipboard(localClipboardManager, url)
        }) {
            Icon(
                imageVector = WebServerIcons.ContentCopy,
                contentDescription = stringResource(Res.string.copy_base_url)
            )
        }
        val platformContext = getPlatformContext()
        IconButton(
            onClick = {
                share(platformContext, url)
            },
            modifier = Modifier
        ) {
            Icon(
                imageVector = WebServerIcons.Share,
                contentDescription = stringResource(Res.string.share_base_ur)
            )
        }
    }
}

private fun clipboard(
    localClipboardManager: ClipboardManager,
    url: String
) {
    localClipboardManager.setText(AnnotatedString(url))
//    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
//        Toast.makeText(context, R.string.copied, Toast.LENGTH_LONG).show()
//    }
}

@Composable
fun ServerStop(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            imageVector = WebServerIcons.Server,
            contentDescription = null,
            modifier = Modifier
                .sizeIn(maxWidth = 96.dp, maxHeight = 96.dp)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
        )
        Text(
            text = stringResource(Res.string.server_title),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(Res.string.start_server_message),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview()
@Composable
fun GreetingPreview() {
    MaterialTheme {
        AddressItem("https://anicehome.github.io/")
    }
}

@Preview()
@Composable
fun UrlItemPreview() {
    MaterialTheme {
        val url = "https://anicehome.github.io/"
        UrlItem(url, imageBitmap = ImageBitmap(0, 0))
    }
}

@Preview()
@Composable
fun FirstScreenPreview() {
    MaterialTheme {
        val serverUrls = listOf("https://anicehome.github.io/", "https://www.join.com")
        FirstScreen(serverUrls, emptyList())
    }
}

@Preview()
@Composable
fun SecondScreenPreview() {
    MaterialTheme {
        val isPlaying = false
        SecondScreen(isPlaying, {}, {})
    }
}

@Preview()
@Composable
fun EmptyServerPreview() {
    MaterialTheme {
        EmptyServer()
    }
}