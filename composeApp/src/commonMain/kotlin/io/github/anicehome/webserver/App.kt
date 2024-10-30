package io.github.anicehome.webserver

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.anicehome.webserver.domain.DarkThemeConfig
import io.github.anicehome.webserver.domain.ThemeBrand
import io.github.anicehome.webserver.home.HomeScreen
import io.github.anicehome.webserver.home.di.homeModule
import io.github.anicehome.webserver.setting.SettingsUiState
import io.github.anicehome.webserver.setting.SettingsViewModel
import io.github.anicehome.webserver.theme.WebServerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

import org.koin.core.context.startKoin
import org.koin.core.module.Module


@Composable
@Preview
fun App(settingsViewModel: SettingsViewModel = koinViewModel()) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    WebServerTheme(
        darkTheme = shouldUseDarkTheme(settingsUiState),
        androidTheme = shouldUseAndroidTheme(settingsUiState),
    ) {
        HomeScreen()
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> isSystemInDarkTheme()
    is SettingsUiState.Success -> when (uiState.settings.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}

@Composable
private fun shouldUseAndroidTheme(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> false
    is SettingsUiState.Success -> when (uiState.settings.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

fun initKoin(appModule: Module) {

    startKoin {
        modules(
            appModule,
            homeModule
        )
    }
}