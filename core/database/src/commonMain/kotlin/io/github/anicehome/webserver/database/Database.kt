package io.github.anicehome.webserver.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

internal fun getRoomDatabase(
    roomDatabaseBuilder: RoomDatabase.Builder<WebServerDatabase>
): WebServerDatabase {
    return roomDatabaseBuilder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

internal const val dbFileName = "webserver.db"