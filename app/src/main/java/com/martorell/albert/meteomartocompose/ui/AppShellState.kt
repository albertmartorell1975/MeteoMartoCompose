package com.martorell.albert.meteomartocompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.ui.navigation.DashboardScreens
import com.martorell.albert.meteomartocompose.ui.navigation.SubGraphs
import com.martorell.albert.meteomartocompose.ui.navigation.shared.TopLevelRoute

/**
 * Handles the mapping logic between navigation destinations and UI shell elements
 * like TopAppBar titles and bottom navigation visibility.
 */
class AppShellState {

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

    fun isBottomBarVisible(destination: NavDestination?): Boolean {
        val currentDest = destination ?: return false
        return BOTTOM_NAV_OPTIONS.any { route ->
            currentDest.hierarchy.any { it.hasRoute(route.route::class) }
        }
    }

    fun getTopAppBarTitle(destination: NavDestination?): Int? {
        val currentDest = destination ?: return null
        return when {
            currentDest.hasRoute(DashboardScreens.CityWeather::class) -> R.string.city_top_bar_title
            currentDest.hasRoute(DashboardScreens.Favorites::class) -> R.string.favorite_top_bar_title
            currentDest.hierarchy.any { it.hasRoute(SubGraphs.FavoritesGraph::class) } -> R.string.city_weather_detail
            else -> null
        }
    }

    fun shouldShowBackButton(destination: NavDestination?): Boolean {
        val currentDest = destination ?: return false
        return currentDest.hierarchy.any { it.hasRoute(SubGraphs.FavoritesGraph::class) }
    }

    fun isFavoriteButtonVisible(destination: NavDestination?): Boolean {
        return destination?.hasRoute(DashboardScreens.CityWeather::class) == true
    }
}
