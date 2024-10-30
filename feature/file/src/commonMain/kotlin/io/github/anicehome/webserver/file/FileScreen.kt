package io.github.anicehome.webserver.file

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.anicehome.webserver.database.WebFile
import io.github.anicehome.webserver.resource.Res
import io.github.anicehome.webserver.resource.feature_settings_loading
import io.github.anicehome.webserver.resource.file_empty_message
import io.github.anicehome.webserver.resource.file_empty_title
import io.github.anicehome.webserver.file.MyShareUiState.Loading
import io.github.anicehome.webserver.resource.WebServerIcons
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FileScreen(fileViewModel: FileViewModel = koinViewModel()) {
    val uiState by fileViewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForResult { result ->
        result.webFiles.forEach {
            fileViewModel.add(webFile = it)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(Res.string.feature_settings_loading),
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }

            is MyShareUiState.Success -> {
                val webFiles = (uiState as MyShareUiState.Success).myShare.webFiles
                if (webFiles.isEmpty()) {
                    EmptyFile()
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp, top = 8.dp, end = 16.dp, bottom = 24.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(webFiles) { webFile ->
                            CategoryCard(webFile) {
                                fileViewModel.remove(it)
                            }
                        }
                    }

                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = {
                launcher.launcher()
            },
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "App settings")
        }
    }
}

@Composable
private fun CategoryCard(webFile: WebFile, remove: (WebFile) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column {
//            Image(
//                contentScale = ContentScale.Crop,
//                bitmap = imageResource(Res.drawable.compose_multiplatform),
//                modifier = Modifier.aspectRatio(1f),
//                contentDescription = webFile.name
//            )
            Column {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                ) {
                    Text(
                        text = webFile.name,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }

                var expanded by remember { mutableStateOf(false) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val formattedDate = dateFormatted(webFile.lastModified)
                    Text(
                        text = formattedDate,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f)
                    )
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                    DropdownMenuItem(
//                        text = { Text("Share") },
//                        onClick = {
//                            expanded = false
//                        },
//                        leadingIcon = { Icon(Icons.Filled.Share, contentDescription = null) }
//                    )
                        DropdownMenuItem(text = { Text("Delete") },
                            onClick = {
                                expanded = false
                                remove.invoke(webFile)
                            },
                            leadingIcon = { Icon(Icons.Filled.Delete, contentDescription = null) })
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFile() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                imageVector = WebServerIcons.File,
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(maxWidth = 96.dp, maxHeight = 96.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
            )
            Text(
                text = stringResource(Res.string.file_empty_title),
                modifier = Modifier.padding(top = 64.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = stringResource(Res.string.file_empty_message),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Preview()
@Composable
fun UrlItemPreview() {
    MaterialTheme {
        CategoryCard(WebFile(
            id = 0,
            uri = "https://anicehome.github.io/",
            name = "abc.mp4",
            size = 1024,
            mimeType = "mp4",
            lastModified = 0,
            icon = 0,
        ), {})
    }
}

@Preview()
@Composable
fun EmptyFilePreview() {
    MaterialTheme {
        EmptyFile()
    }
}
