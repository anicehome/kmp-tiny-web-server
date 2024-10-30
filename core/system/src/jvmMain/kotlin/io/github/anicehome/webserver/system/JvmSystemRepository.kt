package io.github.anicehome.webserver.system

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.InputStream
import java.net.NetworkInterface

class JvmSystemRepository : SystemRepository {
    override fun openInputStream(uriString: String): InputStream {
        return File(uriString).inputStream()
    }

    override fun network(): Flow<List<String>> = callbackFlow {
        val ips: MutableList<String> = mutableListOf()
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val currentInterface = interfaces.nextElement()
            if (currentInterface.isLoopback || !currentInterface.isUp) {
                continue
            }
            val addresses = currentInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val currentAddress = addresses.nextElement()
                if (currentAddress.isSiteLocalAddress) {
                    val ip = currentAddress.hostAddress
                    ips.add(ip)
                }
            }
        }
        trySendBlocking(ips)
        awaitClose {

        }
    }

    override fun takePermission(uriString: String) {

    }

    override fun releasePermission(uriString: String) {

    }
}