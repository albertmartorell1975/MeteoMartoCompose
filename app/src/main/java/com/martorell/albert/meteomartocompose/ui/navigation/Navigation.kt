package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.MainViewModel
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmNavigation
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmNavigationItem
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmSnackbarHost
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndDesignSystemViewModel
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.splash.SplashUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    mainViewModel: MainViewModel = hiltViewModel(),
    designSystemViewModel: FndDesignSystemViewModel = hiltViewModel(),
) {
    val rootState by mainViewModel.state.collectAsStateWithLifecycle()
    val fontScale by designSystemViewModel.fontScale.collectAsStateWithLifecycle()

    if (rootState.isLoading) {
        SplashUI()
    } else {
        val appState: AppState = rememberAppState()
        val currentBackStackEntry = appState.currentBackStackEntry
        val shellChrome = appState.shellChrome
        val dashboardEntry = remember(currentBackStackEntry) {
            appState.dashboardBackStackEntryOrNull(currentBackStackEntry)
        }
        val dashboardViewModel: CityWeatherViewModel? = if (dashboardEntry != null) {
            hiltViewModel(dashboardEntry)
        } else {
            null
        }

        val scrollState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

        LaunchedEffect(currentBackStackEntry?.id) {
            scrollState.heightOffset = 0f
            scrollState.contentOffset = 0f
        }

        MeteoMartoComposeLayout(fontScale = fontScale) {
            val navItems = if (shellChrome.showBottomNavigation) {
                AppState.NAV_OPTIONS.map { topLevelRoute ->
                    MmNavigationItem(
                        route = topLevelRoute.route,
                        icon = topLevelRoute.icon,
                        label = stringResource(topLevelRoute.titleRes),
                    )
                }
            } else {
                emptyList()
            }

            MmNavigation(
                items = navItems,
                isItemSelected = { item ->
                    appState.isTopLevelRouteSelected(currentBackStackEntry, item.route)
                },
                onItemClick = { route ->
                    (route as? DashboardScreens)?.let(appState::onNavItemClick)
                },
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    if (shellChrome.showTopAppBar) {
                        val title = shellChrome.topAppBarTitleRes
                            ?.let { stringResource(it) }
                            .orEmpty()
                        TopAppBarCustom(
                            title = title,
                            showBackButton = shellChrome.showBackButton,
                            onBackClick = appState::navigateUp,
                            showLogoutAction = shellChrome.showLogoutAction,
                            onLogoutClick = { dashboardViewModel?.showLogOutDialog() },
                            scrollBehavior = scrollBehavior,
                        )
                    }
                },
                floatingActionButton = {
                    if (shellChrome.showFavoriteFab && appState.isFabVisible && dashboardViewModel != null) {
                        FloatingActionButton(
                            onClick = {
                                appState.onFabClick { dashboardViewModel.onFavoriteClicked() }
                            },
                        ) {
                            val icon = appState.fabIcon {
                                dashboardViewModel.isCityFavorite()
                            }
                            icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = stringResource(R.string.favorite),
                                )
                            }
                        }
                    }
                },
                snackbarHost = {
                    MmSnackbarHost(hostState = appState.snackbarHostState)
                },
            ) {
                val startDestination = if (rootState.isLoggedIn) SubGraphs.Dashboard else SubGraphs.Auth

                NavHost(
                    navController = appState.navController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    authSubGraph(
                        appState = appState,
                    )

                    dashboardGraph(
                        appState = appState,
                        nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                    ) { newVisibility ->
                        appState.updateIsFabVisible(newVisibility)
                    }
                }
            }
        }
    }
}
