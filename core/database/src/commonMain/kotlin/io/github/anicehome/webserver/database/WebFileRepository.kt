package io.github.anicehome.webserver.database

class WebFileRepository constructor(
    private val webFileDao: WebFileDao,
    private val transactionRunner: TransactionRunner
) {
    suspend fun add(webFile: WebFile) = transactionRunner {
        if (webFileDao.isWebFileNotExist(webFile.uri)) {
            webFileDao.insert(webFile)
        }
    }

    fun webFiles() = webFileDao.webFiles()
    suspend fun webFilesSuspend() = webFileDao.webFilesSuspend()

    suspend fun webFileById(id: Int) = webFileDao.webFileById(id)

    suspend fun remove(webFile: WebFile) = webFileDao.delete(webFile)

    suspend fun webFileSuspend(id: Int) = webFileDao.webFileByIdSuspend(id)
}