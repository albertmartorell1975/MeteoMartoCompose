package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherScreen
import com.martorell.albert.meteomartocompose.ui.screens.city.CityWeatherViewModel
import com.martorell.albert.meteomartocompose.ui.screens.city.HighTemperatureAlertScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesDetailScreen
import com.martorell.albert.meteomartocompose.ui.screens.favorites.FavoritesScreen
import com.martorell.albert.meteomartocompose.utils.AppConstants

fun NavGraphBuilder.dashboardGraph(
    appState: AppState,
    nestedScrollConnection: NestedScrollConnection,
    modifier: Modifier = Modifier,
    setFabVisibility: (isVisible: Boolean) -> Unit,
) {
    navigation<SubGraphs.Dashboard>(
        startDestination = DashboardScreens.CityForecast(),
    ) {
        composable<DashboardScreens.CityForecast> { backStackEntry ->
            val route = backStackEntry.toRoute<DashboardScreens.CityForecast>()
            if (route.isFavoriteDetail) {
                FavoritesDetailScreen(
                    modifier = modifier,
                    onBack = appState::navigateUp,
                )
            } else {
                val parentEntry = remember(backStackEntry) {
                    appState.dashboardBackStackEntryOrNull(backStackEntry) ?: backStackEntry
                }
                val viewModel: CityWeatherViewModel = hiltViewModel(parentEntry)

                CityWeatherScreen(
                    viewModel = viewModel,
                    modifier = modifier,
                    nestedScrollConnection = nestedScrollConnection,
                    goToLogin = appState::navigateToAuth,
                    goToHighTempAlert = appState::navigateToHighTemperatureAlert,
                    setFabVisibility = setFabVisibility,
                )
            }
        }

        composable<DashboardScreens.Favorites> {
            FavoritesScreen(
                modifier = modifier,
                nestedScrollConnection = nestedScrollConnection,
                goToDetail = { city ->
                    city?.name?.takeIf { it.isNotBlank() }?.let(appState::navigateToCityForecast)
                },
            )
        }

        dialog<DashboardScreens.HighTemperatureAlert>(
            deepLinks = listOf(
                navDeepLink<DashboardScreens.HighTemperatureAlert>(
                    basePath = AppConstants.DEEP_LINK_BASE,
                ),
            ),
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<DashboardScreens.HighTemperatureAlert>()
            HighTemperatureAlertScreen(
                temperature = route.temperature,
                onDismiss = appState::navigateUp,
            )
        }
    }
}
