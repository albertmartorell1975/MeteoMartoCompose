package com.martorell.albert.meteomartocompose.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.navigatePoppingUpToStartDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    val shellState = remember { AppShellState() }
    val fabState = remember(coroutineScope) { FabState(coroutineScope) }

    return remember(navController, snackbarHostState, coroutineScope, shellState, fabState) {
        AppState(
            navController = navController,
            snackbarHostState = snackbarHostState,
            shellState = shellState,
            fabState = fabState
        )
    }
}

/**
 * AppState acts as the central orchestrator for the application's UI logic.
 * It follows the composition pattern to delegate specific responsibilities to
 * specialized state holders.
 */
class AppState(
    private val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val shellState: AppShellState,
    private val fabState: FabState
) {
    companion object {
        val BOTTOM_NAV_OPTIONS get() = AppShellState.BOTTOM_NAV_OPTIONS
    }

    // Infrastructure Exposure
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    // Shell Mapping (Delegated to AppShellState)
    val showBottomNavigation: Boolean
        @Composable get() = shellState.isBottomBarVisible(currentDestination)

    val topAppBarTitle: Int?
        @Composable get() = shellState.getTopAppBarTitle(currentDestination)

    val shouldShowBackButton: Boolean
        @Composable get() = shellState.shouldShowBackButton(currentDestination)

    val showFavoriteButton: Boolean
        @Composable get() = shellState.isFavoriteButtonVisible(currentDestination)

    // FAB State & Actions (Delegated to FabState)
    val isFabVisible: Boolean get() = fabState.isVisible

    fun updateIsFabVisible(newValue: Boolean) = fabState.updateVisibility(newValue)

    @Composable
    fun fabIcon(isCityFavoriteAction: suspend () -> Boolean): ImageVector? =
        fabState.getIcon(isCityFavoriteAction)

    fun onFabClick(onFabAction: () -> Unit) = fabState.onFabClick(onFabAction)

    // Navigation Bridge
    fun onNavItemClick(screen: DashboardScreens) {
        navController.navigatePoppingUpToStartDestination(screen)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigate(route: Any, builder: NavOptionsBuilder.() -> Unit = {}) {
        navController.navigate(route, builder)
    }

    fun getBackStackEntry(route: Any) = navController.getBackStackEntry(route)
}
