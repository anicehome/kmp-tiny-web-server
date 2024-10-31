package io.github.anicehome.webserver.util

import io.ktor.http.ContentType
import io.ktor.http.fromFilePath

const val GENERIC_MIMETYPE = "*/*"

fun getMimeType(fileName: String) =
    ContentType.fromFilePath(fileName).firstOrNull()?.toString() ?: GENERIC_MIMETYPE