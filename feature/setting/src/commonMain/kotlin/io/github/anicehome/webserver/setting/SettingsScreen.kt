@file:OptIn(ExperimentalLayoutApi::class)

package io.github.anicehome.webserver.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.anicehome.webserver.domain.DarkThemeConfig
import io.github.anicehome.webserver.domain.ThemeBrand
import io.github.anicehome.webserver.resource.Res.string
import io.github.anicehome.webserver.resource.feature_settings_brand_android
import io.github.anicehome.webserver.resource.feature_settings_brand_default
import io.github.anicehome.webserver.resource.feature_settings_dark_mode_config_dark
import io.github.anicehome.webserver.resource.feature_settings_dark_mode_config_light
import io.github.anicehome.webserver.resource.feature_settings_dark_mode_config_system_default
import io.github.anicehome.webserver.resource.feature_settings_dark_mode_preference
import io.github.anicehome.webserver.resource.feature_settings_loading
import io.github.anicehome.webserver.resource.feature_settings_privacy_policy
import io.github.anicehome.webserver.resource.feature_settings_theme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            when (settingsUiState) {
                SettingsUiState.Loading -> {
                    Text(
                        text = stringResource(string.feature_settings_loading),
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }

                is SettingsUiState.Success -> {
                    SettingsPanel(
                        settings = (settingsUiState as SettingsUiState.Success).settings,
                        onChangeThemeBrand = settingsViewModel::updateThemeBrand,
                        onChangeDarkThemeConfig = settingsViewModel::updateDarkThemeConfig,
                    )
                }
            }
            HorizontalDivider(Modifier.padding(top = 8.dp))
            LinksPanel()
        }
    }
}

@Composable
private fun SettingsPanel(
    settings: UserEditableSettings,
    onChangeThemeBrand: (themeBrand: ThemeBrand) -> Unit,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
) {
    SettingsDialogSectionTitle(text = stringResource(string.feature_settings_theme))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(string.feature_settings_brand_default),
            selected = settings.themeBrand == ThemeBrand.DEFAULT,
            onClick = { onChangeThemeBrand(ThemeBrand.DEFAULT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.feature_settings_brand_android),
            selected = settings.themeBrand == ThemeBrand.ANDROID,
            onClick = { onChangeThemeBrand(ThemeBrand.ANDROID) },
        )
    }
    SettingsDialogSectionTitle(text = stringResource(string.feature_settings_dark_mode_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(string.feature_settings_dark_mode_config_system_default),
            selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.feature_settings_dark_mode_config_light),
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.feature_settings_dark_mode_config_dark),
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
private fun LinksPanel() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        TextButton(
            onClick = {
                uriHandler.openUri(PRIVACY_POLICY_URL)
            },
        ) {
            Text(text = stringResource(string.feature_settings_privacy_policy))
        }
//        val context = LocalContext.current
//        TextButton(
//            onClick = {
////                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
//            },
//        ) {
//            Text(text = stringResource(string.feature_settings_licenses))
//        }
//        TextButton(
//            onClick = {
////                uriHandler.openUri(BRAND_GUIDELINES_URL)
//            },
//        ) {
//            Text(text = stringResource(string.feature_settings_brand_guidelines))
//        }
//        TextButton(
//            onClick = {
////                uriHandler.openUri(FEEDBACK_URL)
//            },
//        ) {
//            Text(text = stringResource(string.feature_settings_feedback))
//        }
    }
}
private const val PRIVACY_POLICY_URL = "https://anicehome.github.io/privacy"