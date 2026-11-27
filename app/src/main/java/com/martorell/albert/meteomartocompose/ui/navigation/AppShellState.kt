package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.Immutable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.martorell.albert.meteomartocompose.R

/** Immutable chrome visibility and action flags derived for the active navigation destination. */
@Immutable
data class AppShellChrome(
    val showBottomNavigation: Boolean = false,
    val showTopAppBar: Boolean = false,
    @param:StringRes val topAppBarTitleRes: Int? = null,
    val showBackButton: Boolean = false,
    val showLogoutAction: Boolean = false,
    val showFavoriteFab: Boolean = false,
)

internal sealed interface ShellDestination {
    data object CurrentLocationForecast : ShellDestination
    data object FavoriteCityDetail : ShellDestination
    data object FavoritesList : ShellDestination
    data object Other : ShellDestination
}

/** Maps the active [NavBackStackEntry] to deterministic [AppShellChrome] and top-level tab selection state. */
class AppShellState {

    fun resolveChrome(entry: NavBackStackEntry?): AppShellChrome =
        resolveChrome(classifyDestination(entry))

    internal fun resolveChrome(destination: ShellDestination): AppShellChrome = when (destination) {
        ShellDestination.CurrentLocationForecast -> AppShellChrome(
            showBottomNavigation = true,
            showTopAppBar = true,
            topAppBarTitleRes = R.string.city_top_bar_title,
            showBackButton = false,
            showLogoutAction = true,
            showFavoriteFab = true,
        )

        ShellDestination.FavoritesList -> AppShellChrome(
            showBottomNavigation = true,
            showTopAppBar = true,
            topAppBarTitleRes = R.string.favorite_top_bar_title,
            showBackButton = false,
            showLogoutAction = false,
            showFavoriteFab = false,
        )

        ShellDestination.FavoriteCityDetail -> AppShellChrome(
            showBottomNavigation = false,
            showTopAppBar = true,
            topAppBarTitleRes = R.string.city_weather_detail,
            showBackButton = true,
            showLogoutAction = false,
            showFavoriteFab = false,
        )

        ShellDestination.Other -> AppShellChrome()
    }

    fun isTopLevelRouteSelected(entry: NavBackStackEntry?, route: Any): Boolean =
        isTopLevelRouteSelected(classifyDestination(entry), route)

    internal fun isTopLevelRouteSelected(destination: ShellDestination, route: Any): Boolean =
        when (destination) {
            ShellDestination.CurrentLocationForecast ->
                route is DashboardScreens.CityForecast && !route.isFavoriteDetail

            ShellDestination.FavoritesList -> route is DashboardScreens.Favorites
            ShellDestination.FavoriteCityDetail,
            ShellDestination.Other -> false
        }

    private fun classifyDestination(entry: NavBackStackEntry?): ShellDestination {
        val destination = entry?.destination ?: return ShellDestination.Other
        return when {
            destination.hasRoute<DashboardScreens.CityForecast>() -> {
                val route = entry.toRoute<DashboardScreens.CityForecast>()
                if (route.isFavoriteDetail) {
                    ShellDestination.FavoriteCityDetail
                } else {
                    ShellDestination.CurrentLocationForecast
                }
            }

            destination.hasRoute<DashboardScreens.Favorites>() -> ShellDestination.FavoritesList
            else -> ShellDestination.Other
        }
    }

    companion object {
        val NAV_OPTIONS: List<TopLevelRoute<DashboardScreens>> = listOf(
            TopLevelRoute(
                titleRes = R.string.city_tab,
                route = DashboardScreens.CityForecast(),
                icon = Icons.Default.LocationCity,
            ),
            TopLevelRoute(
                titleRes = R.string.favorite_tab,
                route = DashboardScreens.Favorites,
                icon = Icons.Default.Favorite,
            ),
        )
    }
}
