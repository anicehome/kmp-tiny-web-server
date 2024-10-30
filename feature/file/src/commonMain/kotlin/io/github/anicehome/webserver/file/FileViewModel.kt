package io.github.anicehome.webserver.file

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.anicehome.webserver.database.WebFile
import io.github.anicehome.webserver.database.WebFileRepository
import io.github.anicehome.webserver.system.SystemRepository
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class FileViewModel constructor(
    private val webFileRepository: WebFileRepository,
    private val systemRepository: SystemRepository,
) : ViewModel() {
    private val _uiState = webFileRepository.webFiles().map {
        MyShareUiState.Success(MyShare(it))
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = MyShareUiState.Loading,
    )
    val uiState: StateFlow<MyShareUiState>
        get() = _uiState

    fun remove(webFile: WebFile) {
        viewModelScope.launch {
            webFileRepository.remove(webFile)
            systemRepository.releasePermission(webFile.uri)
        }
    }

    fun add(webFile: WebFile) {
        viewModelScope.launch {
            systemRepository.takePermission(webFile.uri)
            webFileRepository.add(webFile)
        }
    }
}

data class MyShare(
    val webFiles: List<WebFile> = emptyList(),
)

sealed interface MyShareUiState {
    data object Loading : MyShareUiState
    data class Success(val myShare: MyShare) : MyShareUiState
}