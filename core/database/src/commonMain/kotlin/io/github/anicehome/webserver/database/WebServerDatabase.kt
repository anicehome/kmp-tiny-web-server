package io.github.anicehome.webserver.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        WebFile::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class WebServerDatabase : RoomDatabase() {

    abstract fun webFileDao(): WebFileDao

    abstract fun transactionRunnerDao(): TransactionRunnerDao
}
