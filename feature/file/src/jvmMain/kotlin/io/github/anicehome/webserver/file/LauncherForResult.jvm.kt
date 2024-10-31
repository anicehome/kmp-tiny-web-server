package io.github.anicehome.webserver.file

import androidx.compose.runtime.Composable
import io.github.anicehome.webserver.database.WebFile
import io.github.anicehome.webserver.util.getMimeType
import javax.swing.JFileChooser

class JvmLauncher(private val result: (Result) -> Unit) : Launcher {
    private val jFileChooser = JFileChooser()

    init {
        jFileChooser.dialogTitle = "Please choose a path"
        jFileChooser.isMultiSelectionEnabled = true
    }

    override fun launcher() {
        val state = jFileChooser.showOpenDialog(null)
        if (state == JFileChooser.APPROVE_OPTION) {
            val selectedFiles = jFileChooser.selectedFiles

            val webFiles = selectedFiles.map { file ->
                val mimeType = getMimeType(file.name)
                WebFile(
                    id = 0,
                    uri = file.absolutePath,
                    name = file.name,
                    size = file.length(),
                    mimeType = mimeType,
                    lastModified = file.lastModified(),
                    icon = 0,
                )
            }
            result.invoke(
                Result(
                    webFiles = webFiles
                )
            )
        }

    }
}

@Composable
actual fun rememberLauncherForResult(result: (Result) -> Unit): Launcher {
    return JvmLauncher(result)
}
