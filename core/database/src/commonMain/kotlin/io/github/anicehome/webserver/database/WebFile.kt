package io.github.anicehome.webserver.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "web_file",
    indices = [
        Index("id", unique = true)
    ]
)
@TypeConverters(WebServerTypeConverters::class)
data class WebFile(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "mimeType") val mimeType: String,
    @ColumnInfo(name = "lastModified") val lastModified: Long,
    @ColumnInfo(name = "icon") val icon: Int,
)