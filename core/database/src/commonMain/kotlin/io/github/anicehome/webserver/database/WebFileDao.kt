package io.github.anicehome.webserver.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WebFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(webFile: WebFile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg webFile: WebFile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(webFiles: Collection<WebFile>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(webFile: WebFile)

    @Delete
    suspend fun delete(webFile: WebFile): Int

    @Query("SELECT COUNT(*) FROM web_file")
    suspend fun count(): Int

    @Query("SELECT * FROM web_file WHERE id = :id")
    fun webFileById(id: Int): Flow<WebFile>

    @Query("SELECT * FROM web_file WHERE id = :id")
    suspend fun webFileByIdSuspend(id: Int): WebFile

    @Query("SELECT * FROM web_file")
    fun webFiles(): Flow<List<WebFile>>

    @Query("SELECT * FROM web_file")
    suspend fun webFilesSuspend(): List<WebFile>

    @Query("SELECT COUNT(*) FROM web_file WHERE uri = :uri")
    suspend fun webFileRowCount(uri: String): Int

    suspend fun isWebFileNotExist(path: String): Boolean {
        return webFileRowCount(path) == 0
    }
}