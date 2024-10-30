package io.github.anicehome.webserver.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.github.anicehome.webserver.domain.DarkThemeConfig
import io.github.anicehome.webserver.domain.ThemeBrand
import io.github.anicehome.webserver.domain.UserData
import kotlinx.coroutines.flow.map

class PreferencesDataSource constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val themeBrandKey = intPreferencesKey("theme_rand")
    private val darkThemeConfigKey = intPreferencesKey("dark_theme_config")


    val userData = dataStore.data
        .map {
            val themeBrand = when (it[themeBrandKey]) {
                ThemeBrand.DEFAULT.value -> ThemeBrand.DEFAULT
                ThemeBrand.ANDROID.value -> ThemeBrand.ANDROID
                else -> ThemeBrand.DEFAULT
            }
            val darkThemeConfig = when (it[darkThemeConfigKey]) {
                DarkThemeConfig.FOLLOW_SYSTEM.value -> DarkThemeConfig.FOLLOW_SYSTEM
                DarkThemeConfig.LIGHT.value -> DarkThemeConfig.LIGHT
                DarkThemeConfig.DARK.value -> DarkThemeConfig.DARK
                else -> DarkThemeConfig.FOLLOW_SYSTEM
            }
            UserData(
                themeBrand = themeBrand,
                darkThemeConfig = darkThemeConfig,
            )
        }

    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        dataStore.edit {
            it[themeBrandKey] = themeBrand.value
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.edit {
            it[darkThemeConfigKey] = darkThemeConfig.value
        }
    }

}