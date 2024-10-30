package io.github.anicehome.webserver.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

internal fun jvmDataStore(): DataStore<Preferences> = createDataStore(
    path = {
        File(System.getProperty("java.io.tmpdir"), dataStoreFileName).path
    },
)