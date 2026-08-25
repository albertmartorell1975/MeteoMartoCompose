package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.AppState
import com.martorell.albert.meteomartocompose.ui.MainViewModel
import com.martorell.albert.meteomartocompose.ui.MeteoMartoComposeLayout
import com.martorell.albert.meteomartocompose.ui.navigation.shared.NavigationBarCustom
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopAppBarCustom
import com.martorell.albert.meteomartocompose.ui.rememberAppState
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.splash.SplashUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val rootState by mainViewModel.state.collectAsState()

    if (rootState.isLoading) {
        SplashUI()
    } else {
        val appState: AppState = rememberAppState(navController = navController)
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // Scoping ViewModel to the Dashboard subgraph to avoid leaks between sessions
        val dashboardEntry = remember(navBackStackEntry) {
            try {
                navController.getBackStackEntry(SubGraphs.Dashboard)
            } catch (e: Exception) {
                null
            }
        }

        val scrollState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(scrollState)

        MeteoMartoComposeLayout {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    if (appState.showBottomNavigation) {
                        TopAppBarCustom(
                            navController = navController,
                            scrollBehavior = scrollBehavior
                        )
                    }
                },
                bottomBar = {
                    if (appState.showBottomNavigation) {
                        NavigationBarCustom(appState = appState)
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
                }
            ) { innerPadding ->
                val startDestination = if (rootState.isLoggedIn) SubGraphs.Dashboard else SubGraphs.Auth

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    authSubGraph(navController = navController)

                    dashboardGraph(
                        navController = navController,
                        setFabVisibility = { newVisibility ->
                            appState.updateIsFabVisible(newVisibility)
                        }
                    )

                    favoriteSubGraph(navController = navController)
                }
            }
        }
    }
}
