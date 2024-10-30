@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.anicehome.webserver.home

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import io.github.anicehome.webserver.file.FileScreen
import io.github.anicehome.webserver.setting.SettingsScreen
import io.github.anicehome.webserver.service.ServiceScreenBox
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.enums.EnumEntries

@Composable
fun HomeScreen() {

    val adaptiveInfo = currentWindowAdaptiveInfo()
    val customNavSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)

    val topLevelDestinations = TopLevelDestination.entries
    val pagerState =
        rememberPagerState(initialPage = TopLevelDestination.SERVER.ordinal) { topLevelDestinations.size }
    val coroutineScope = rememberCoroutineScope()

    NavigationSuiteScaffold(
        modifier = Modifier,
        navigationSuiteItems = {
            topLevelDestinations.forEachIndexed { index, destination ->
                item(icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = stringResource(destination.iconTextId),
                    )
                },
                    label = { Text(stringResource(destination.iconTextId)) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        },
        layoutType = customNavSuiteType
    ) {
        when (customNavSuiteType) {
            NavigationSuiteType.NavigationBar -> {
                HorizontalPager(
                    state = pagerState,
                    pageContent = pageContent(topLevelDestinations)
                )
            }

            else -> {
                VerticalPager(
                    state = pagerState,
                    pageContent = pageContent(topLevelDestinations)
                )
            }
        }
    }
}

private fun pageContent(
    topLevelDestinations: EnumEntries<TopLevelDestination>,
): @Composable (PagerScope.(page: Int) -> Unit) =
    { page ->
        when (topLevelDestinations[page]) {
//            WebServerScreen.Message -> {
//                MessageScreen()
//            }

            TopLevelDestination.FILE -> {
                FileScreen()
            }

            TopLevelDestination.SERVER -> {
                ServiceScreenBox()
            }

            TopLevelDestination.SETTING -> {
                SettingsScreen()
            }
        }
    }