package io.github.anicehome.webserver.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

internal fun androidDatabaseBuilder(context: Context): RoomDatabase.Builder<WebServerDatabase> {
    val dbFile = context.getDatabasePath(dbFileName)
    return Room.databaseBuilder<WebServerDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}