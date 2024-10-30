package io.github.anicehome.webserver.system

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.InputStream

class AndroidSystemRepository(private val context: Context) : SystemRepository {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    override fun openInputStream(uriString: String): InputStream {
        val uri = Uri.parse(uriString)
        return context.contentResolver.openInputStream(uri)!!
    }

    override fun network(): Flow<List<String>> = callbackFlow {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySendBlocking(linkAddress(network))
            }

            override fun onLost(network: Network) {
                trySendBlocking(linkAddress(network))
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            }
        }

        connectivityManager.activeNetwork?.let { trySendBlocking(linkAddress(it)) }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun linkAddress(network: Network): List<String> {
        val links = mutableListOf<String>()
        val linkProperties = connectivityManager.getLinkProperties(network)
        linkProperties?.let { linkProperties ->
            linkProperties.linkAddresses.forEach {
                it?.let {
                    it.address.hostAddress?.let {
                        links.add(element = it)
                    }
                }
            }
        }
        return links
    }

    override fun takePermission(uriString: String) {
        context.contentResolver.takePersistableUriPermission(
            Uri.parse(uriString),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    override fun releasePermission(uriString: String) {
        context.contentResolver.releasePersistableUriPermission(
            Uri.parse(uriString),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}