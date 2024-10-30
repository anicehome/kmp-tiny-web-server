package io.github.anicehome.webserver.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.anicehome.webserver.domain.DarkThemeConfig
import io.github.anicehome.webserver.domain.ThemeBrand
import io.github.anicehome.webserver.datastore.PreferencesDataSource
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SettingsViewModel constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        preferencesDataSource.userData
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        themeBrand = userData.themeBrand,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingsUiState.Loading,
            )

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            preferencesDataSource.setThemeBrand(themeBrand)
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            preferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        }
    }

}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
