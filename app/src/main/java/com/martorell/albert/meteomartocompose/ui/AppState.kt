package com.martorell.albert.meteomartocompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.navigatePoppingUpToStartDestination
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopLevelRoute

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState = remember(navController) {
    AppState(navController)
}

class AppState(
    private val navController: NavHostController,
) {

    companion object {

        val BOTTOM_NAV_OPTIONS = listOf(
            TopLevelRoute(
                R.string.city_tab,
                DashboardScreens.CityWeather,
                Icons.Default.LocationCity
            ),
            TopLevelRoute(
                R.string.favorite_tab,
                DashboardScreens.Favorites,
                Icons.Default.Favorite
            )
        )

    }

    val showFavoriteButton: Boolean
        @Composable get() {

            BOTTOM_NAV_OPTIONS.forEach { _ ->

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination ?: return false
                currentDestination.route?.let {
                    return it.contains(stringResource(id = R.string.city_tab))
                }

            }

            return false

        }

    val showBottomNavigation: Boolean
        @Composable get() {

            BOTTOM_NAV_OPTIONS.forEach { destination ->

                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry.value?.destination ?: return true

                if (currentDestination.hierarchy.any {
                        it.hasRoute(destination.route::class)
                    } == true)
                    return true

            }

            return false

        }

    fun onNavItemClick(screen: DashboardScreens) {
        navController.navigatePoppingUpToStartDestination(screen)
    }

    @Composable
    fun selectCurrentDestination(screen: DashboardScreens): Boolean {

        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentDestination = navBackStackEntry?.destination
        return currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true

    }

}