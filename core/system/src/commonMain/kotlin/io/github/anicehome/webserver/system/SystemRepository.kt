package io.github.anicehome.webserver.system

import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface SystemRepository {
    fun openInputStream(uriString: String): InputStream

    fun network(): Flow<List<String>>

    fun takePermission(uriString: String)

    fun releasePermission(uriString: String)
}