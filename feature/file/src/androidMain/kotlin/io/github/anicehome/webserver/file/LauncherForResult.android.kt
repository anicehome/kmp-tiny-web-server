package io.github.anicehome.webserver.file

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.anicehome.webserver.database.WebFile

const val GENERIC_MIMETYPE = "*/*"

class AndroidLauncher(
    private val result: ManagedActivityResultLauncher<Array<String>, List<Uri>>,
) : Launcher {
    override fun launcher() {
        result.launch(arrayOf(GENERIC_MIMETYPE))
    }
}

@Composable
internal actual fun rememberLauncherForResult(result: (Result) -> Unit): Launcher {
    val context = LocalContext.current
    val selectFile =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            val webFiles = uris.map { uri ->
                parseWebFile(context.contentResolver, uri)
            }
            result.invoke(
                Result(
                    webFiles = webFiles
                )
            )
        }
    return AndroidLauncher(selectFile)
}

private fun parseWebFile(contentResolver: ContentResolver, uri: Uri): WebFile {
    contentResolver.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
    val projection = arrayOf(
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        DocumentsContract.Document.COLUMN_SIZE,
        DocumentsContract.Document.COLUMN_MIME_TYPE,
        DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        DocumentsContract.Document.COLUMN_ICON,
    )

    val cursor = contentResolver.query(
        uri,
        projection,
        null,
        null,
        null
    ) ?: throw Exception("Uri $uri could not be found")

    cursor.use {
        if (!cursor.moveToFirst()) {
            throw Exception("Uri $uri could not be found")
        }

        val displayNameColumn =
            cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
        val sizeColumn =
            cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_SIZE)
        val mimeTypeColumn =
            cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_MIME_TYPE)
        val lastModifiedColumn =
            cursor.getColumnIndexOrThrow(DocumentsContract.Document.COLUMN_LAST_MODIFIED)

        val filename = cursor.getString(displayNameColumn)
        val size = cursor.getLong(sizeColumn)
        val mimeType = cursor.getString(mimeTypeColumn)
        val lastModified = cursor.getLong(lastModifiedColumn)
        val icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentResolver.getTypeInfo(mimeType).icon.resId
        } else {
//                R.drawable.baseline_connect_without_contact_24
            0
        }
//        val documentThumbnail = DocumentsContract.getDocumentThumbnail(
//            contentResolver,
//            uri,
//            Point(512, 512),
//            null
//        )
        return WebFile(
            id = 0,
            uri = uri.toString(),
            name = filename,
            size = size,
            mimeType = mimeType,
            lastModified = lastModified,
            icon = icon,
//                thumbnail = documentThumbnail,
        )
    }
}