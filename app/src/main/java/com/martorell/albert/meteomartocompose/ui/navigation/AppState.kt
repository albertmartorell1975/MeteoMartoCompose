package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import com.martorell.albert.meteomartocompose.ui.FabState
import kotlinx.coroutines.CoroutineScope

/** Creates and remembers an [AppState] bound to the current Compose [NavHostController] and [WindowSizeClass]. */
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
): AppState {
    val shellState = remember { AppShellState() }
    val fabState = remember(coroutineScope) { FabState(coroutineScope) }

    return remember(
        navController,
        snackbarHostState,
        coroutineScope,
        windowSizeClass,
        shellState,
        fabState,
    ) {
        AppState(
            navController = navController,
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope,
            windowSizeClass = windowSizeClass,
            shellState = shellState,
            fabState = fabState,
        )
    }
}

/**
 * Hoisted navigation shell state holder owning intent-driven navigation, adaptive [windowSizeClass],
 * shell chrome resolution, and favorite FAB visibility.
 */
@Stable
class AppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    private val shellState: AppShellState,
    private val fabState: FabState,
) {

    val currentBackStackEntry: NavBackStackEntry?
        @Composable get() = navController.currentBackStackEntryAsState().value

    val shellChrome: AppShellChrome
        @Composable get() = shellState.resolveChrome(currentBackStackEntry)

    val isFabVisible: Boolean
        get() = fabState.isVisible

    fun isTopLevelRouteSelected(entry: NavBackStackEntry?, route: Any): Boolean =
        shellState.isTopLevelRouteSelected(entry, route)

    fun dashboardBackStackEntryOrNull(currentEntry: NavBackStackEntry?): NavBackStackEntry? {
        val isDashboardInHierarchy = currentEntry?.destination?.hierarchy?.any {
            it.hasRoute<SubGraphs.Dashboard>()
        } == true

        return if (isDashboardInHierarchy) {
            navController.getBackStackEntry<SubGraphs.Dashboard>()
        } else {
            null
        }
    }

    fun updateIsFabVisible(newValue: Boolean) = fabState.updateVisibility(newValue)

    @Composable
    fun fabIcon(isCityFavoriteAction: suspend () -> Boolean): ImageVector? =
        fabState.getIcon(isCityFavoriteAction)

    fun onFabClick(onFabAction: () -> Unit) = fabState.onFabClick(onFabAction)

    fun onNavItemClick(screen: DashboardScreens) {
        navController.navigatePoppingUpToStartDestination<DashboardScreens.CityForecast>(screen)
    }

    fun navigateToDashboard() {
        navController.navigate(SubGraphs.Dashboard) {
            popUpTo<SubGraphs.Auth> {
                inclusive = true
            }
        }
    }

    fun navigateToAuth() {
        navController.navigate(SubGraphs.Auth) {
            popUpTo<SubGraphs.Dashboard> {
                inclusive = true
            }
        }
    }

    fun navigateToSignUp() {
        navController.navigate(AuthScreens.SignUp) {
            launchSingleTop = true
        }
    }

    fun navigateToTerms() {
        navController.navigate(AuthScreens.Terms) {
            launchSingleTop = true
        }
    }

    fun navigateToCityForecast(cityName: String?) {
        navController.navigate(DashboardScreens.CityForecast.fromCityName(cityName)) {
            launchSingleTop = true
        }
    }

    fun navigateToHighTemperatureAlert(temperature: Double) {
        navController.navigate(DashboardScreens.HighTemperatureAlert(temperature)) {
            launchSingleTop = true
        }
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    companion object {
        val NAV_OPTIONS get() = AppShellState.NAV_OPTIONS
    }
}
