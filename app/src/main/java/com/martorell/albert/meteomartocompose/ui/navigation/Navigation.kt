package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MainViewModel
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmNavigation
import com.martorell.albert.meteomartocompose.ui.designsystem.components.MmNavigationItem
import com.martorell.albert.meteomartocompose.ui.designsystem.foundation.FndDesignSystemViewModel
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.rememberAppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.splash.SplashUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    mainViewModel: MainViewModel = hiltViewModel(),
    designSystemViewModel: FndDesignSystemViewModel = hiltViewModel(),
) {
    val rootState by mainViewModel.state.collectAsState()
    val fontScale by designSystemViewModel.fontScale.collectAsState()

    if (rootState.isLoading) {
        SplashUI()
    } else {
        val navController = rememberNavController()
        val appState: AppState = rememberAppState(navController = navController)
        val currentDestination = appState.currentDestination

        // Scoping ViewModel to the Dashboard subgraph to avoid leaks between sessions.
        // We check the hierarchy to safely obtain the entry only when the subgraph is active.
        val dashboardEntry = remember(currentDestination) {
            val isDashboardInHierarchy = currentDestination?.hierarchy?.any {
                it.hasRoute<SubGraphs.Dashboard>()
            } == true

            if (isDashboardInHierarchy) {
                appState.getBackStackEntry(SubGraphs.Dashboard)
            } else {
                null
            }
        }

        val scrollState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

        MeteoMartoComposeLayout(fontScale = fontScale) {
            val navItems = if (appState.showBottomNavigation) {
                AppState.BOTTOM_NAV_OPTIONS.map { topLevelRoute ->
                    MmNavigationItem(
                        route = topLevelRoute.route,
                        icon = topLevelRoute.icon,
                        label = stringResource(topLevelRoute.title),
                    )
                }
            } else {
                emptyList()
            }

            MmNavigation(
                items = navItems,
                currentRoute = currentDestination,
                onItemClick = { route ->
                    (route as? DashboardScreens)?.let {
                        appState.onNavItemClick(it)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    if (appState.showBottomNavigation || appState.shouldShowBackButton) {
                        TopAppBarCustom(
                            appState = appState,
                            scrollBehavior = scrollBehavior,
                        )
                    }
                },
                floatingActionButton = {
                    if (appState.showFavoriteButton && appState.isFabVisible && (dashboardEntry != null)) {
                        val cityWeatherViewModel: CityWeatherViewModel = hiltViewModel(dashboardEntry)
                        FloatingActionButton(
                            onClick = {
                                appState.onFabClick { cityWeatherViewModel.onFavoriteClicked() }
                            }
                        ) {
                            val icon = appState.fabIcon {
                                cityWeatherViewModel.isCityFavorite()
                            }
                            icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = stringResource(R.string.favorite)
                                )
                            }
                        }
                    }
                },
                snackbarHost = {
                    SnackbarHost(appState.snackbarHostState)
                }
            ) {
                val startDestination = if (rootState.isLoggedIn) SubGraphs.Dashboard else SubGraphs.Auth

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    authSubGraph(
                        appState = appState
                    )

                    dashboardGraph(
                        appState = appState,
                        nestedScrollConnection = scrollBehavior.nestedScrollConnection
                    ) { newVisibility ->
                        appState.updateIsFabVisible(newVisibility)
                    }

                    favoriteSubGraph(
                        appState = appState,
                        nestedScrollConnection = scrollBehavior.nestedScrollConnection
                    )
                }
            }
        }
    }
}
