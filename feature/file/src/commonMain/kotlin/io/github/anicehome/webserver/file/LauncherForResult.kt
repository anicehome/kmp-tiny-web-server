package io.github.anicehome.webserver.file

import androidx.compose.runtime.Composable
import io.github.anicehome.webserver.database.WebFile


interface Launcher {
    fun launcher()
}

class Result(val webFiles: List<WebFile>)

@Composable
internal expect fun rememberLauncherForResult(result: (Result) -> Unit): Launcher