package io.github.anicehome.webserver.server

import io.github.anicehome.webserver.database.WebFile
import io.github.anicehome.webserver.database.WebFileRepository
import io.github.anicehome.webserver.system.SystemRepository
import io.ktor.http.ContentDisposition
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fromFilePath
import io.ktor.server.application.*
import io.ktor.server.html.respondHtml
//import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.request.path
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.hr
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr
import java.text.SimpleDateFormat
import java.util.Date

const val route_files = "/files"
const val route_download = "/download"
const val route_id = "/"


fun Application.configureRouting(webFileRepository: WebFileRepository, systemRepository: SystemRepository) {
    routing {
//        singlePageApplication {
//            useResources = true
//            filesPath = "/assets/"
//            defaultPage = "index.html"
//        }

        get("/") {
            call.respondRedirect(route_files)
        }

        route(route_files) {
            listing(webFileRepository)
        }

        get("$route_files$route_download$route_id{id}") {
            val userAgent :String = call.request.headers["User-Agent"].toString()
            val id: Int = call.parameters["id"]?.toInt() ?: -1
            val webFile = webFileRepository.webFileSuspend(id)
            val fileName: String = webFile.name


            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, fileName)
                    .toString()
            )
            val openInputStream = systemRepository.openInputStream(webFile.uri)
            call.respondOutputStream(
                contentType = ContentType.parse(webFile.mimeType),
                status = HttpStatusCode.OK,
                contentLength = webFile.size
            ) {
                use { outputStream ->
                    openInputStream.use { inputStream ->
                        inputStream?.copyTo(outputStream) ?: {}
                    }
                }
            }
        }
    }
}

suspend fun getFileInfos(webFileRepository:WebFileRepository): List<WebFile> {
    return withContext(Dispatchers.IO) {
        webFileRepository.webFilesSuspend()
    }
}

fun Route.listing(webFileRepository:WebFileRepository) {
    val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
    get {
        val userAgent :String = call.request.headers["User-Agent"].toString()


        val files = getFileInfos(webFileRepository)
        val base = call.request.path().trimEnd('/')
        val deviceName = "My Device"
        call.respondHtml {
            body {
                h1 {
                    + "Files on ${deviceName}"
                }
                hr {}
                table {
                    style = "width: 100%;"
                    thead {
                        tr {
                            for (column in listOf(
                                "Name",
                                "Last Modified",
                                "Size",
                                "MimeType"
                            )) {
                                th {
                                    style = "width: 25%; text-align: left;"
                                    +column
                                }
                            }
                        }
                    }
                    tbody {
                        for (finfo in files) {
                            val rname = finfo.name
                            tr {
                                td {
                                    a("$base$route_download$route_id${finfo.id}") { +rname }
                                }
                                td {
                                    +dateFormat.format(Date(finfo.lastModified))
                                }
                                td {
                                    +"${finfo.size}"
                                }
                                td {
                                    +(ContentType.fromFilePath(finfo.name).firstOrNull()
                                        ?.toString() ?: "-")
                                }
                            }
                        }
                    }
                }
                hr {}
            }
        }
    }
}

fun <T> comparators(vararg comparators: java.util.Comparator<T>): java.util.Comparator<T> {
    return Comparator { l, r ->
        for (comparator in comparators) {
            val result = comparator.compare(l, r)
            if (result != 0) return@Comparator result
        }
        return@Comparator 0
    }
}

operator fun <T> java.util.Comparator<T>.plus(other: java.util.Comparator<T>): java.util.Comparator<T> = comparators(this, other)