package io.github.anicehome.webserver.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal fun androidDataStore(context: Context): DataStore<Preferences> = createDataStore(
    path = {
        context.filesDir.resolve(dataStoreFileName).path
    },
)
