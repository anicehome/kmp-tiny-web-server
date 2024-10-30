package io.github.anicehome.webserver.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal fun jvmDatabaseBuilder(): RoomDatabase.Builder<WebServerDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbFileName)
    return Room.databaseBuilder<WebServerDatabase>(
        name = dbFile.absolutePath,
    )
}