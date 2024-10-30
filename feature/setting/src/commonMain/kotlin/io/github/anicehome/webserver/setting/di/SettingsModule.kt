package io.github.anicehome.webserver.setting.di

import io.github.anicehome.webserver.datastore.di.preferencesModule
import io.github.anicehome.webserver.setting.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    includes(preferencesModule)
    viewModelOf(::SettingsViewModel)
}
