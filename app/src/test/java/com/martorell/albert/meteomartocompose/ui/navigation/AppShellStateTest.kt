package com.martorell.albert.meteomartocompose.ui.navigation

import com.martorell.albert.meteomartocompose.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppShellStateTest {

    private val shellState = AppShellState()

    @Test
    fun `resolveChrome and tab selection map current forecast favorite detail favorites list and other routes`() {
        val currentLocationChrome = shellState.resolveChrome(ShellDestination.CurrentLocationForecast)
        assertEquals(
            AppShellChrome(
                showBottomNavigation = true,
                showTopAppBar = true,
                topAppBarTitleRes = R.string.city_top_bar_title,
                showBackButton = false,
                showLogoutAction = true,
                showFavoriteFab = true,
            ),
            currentLocationChrome,
        )
        assertTrue(
            shellState.isTopLevelRouteSelected(
                ShellDestination.CurrentLocationForecast,
                DashboardScreens.CityForecast(),
            ),
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.CurrentLocationForecast,
                DashboardScreens.CityForecast(cityName = "Barcelona"),
            ),
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.CurrentLocationForecast,
                DashboardScreens.Favorites,
            ),
        )

        val favoriteDetailChrome = shellState.resolveChrome(ShellDestination.FavoriteCityDetail)
        assertEquals(
            AppShellChrome(
                showBottomNavigation = false,
                showTopAppBar = true,
                topAppBarTitleRes = R.string.city_weather_detail,
                showBackButton = true,
                showLogoutAction = false,
                showFavoriteFab = false,
            ),
            favoriteDetailChrome,
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.FavoriteCityDetail,
                DashboardScreens.CityForecast(),
            ),
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.FavoriteCityDetail,
                DashboardScreens.Favorites,
            ),
        )

        val favoritesListChrome = shellState.resolveChrome(ShellDestination.FavoritesList)
        assertEquals(
            AppShellChrome(
                showBottomNavigation = true,
                showTopAppBar = true,
                topAppBarTitleRes = R.string.favorite_top_bar_title,
                showBackButton = false,
                showLogoutAction = false,
                showFavoriteFab = false,
            ),
            favoritesListChrome,
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.FavoritesList,
                DashboardScreens.CityForecast(),
            ),
        )
        assertTrue(
            shellState.isTopLevelRouteSelected(
                ShellDestination.FavoritesList,
                DashboardScreens.Favorites,
            ),
        )

        val otherChrome = shellState.resolveChrome(ShellDestination.Other)
        assertEquals(AppShellChrome(), otherChrome)
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.Other,
                DashboardScreens.CityForecast(),
            ),
        )
        assertFalse(
            shellState.isTopLevelRouteSelected(
                ShellDestination.Other,
                DashboardScreens.Favorites,
            ),
        )
    }

    @Test
    fun `cityForecast route contract normalizes cityName and preserves SavedStateHandle key`() {
        val fromNull = DashboardScreens.CityForecast.fromCityName(null)
        assertNull(fromNull.cityName)
        assertFalse(fromNull.isFavoriteDetail)

        val fromEmpty = DashboardScreens.CityForecast.fromCityName("")
        assertNull(fromEmpty.cityName)
        assertFalse(fromEmpty.isFavoriteDetail)

        val fromBlank = DashboardScreens.CityForecast.fromCityName("   ")
        assertNull(fromBlank.cityName)
        assertFalse(fromBlank.isFavoriteDetail)

        val fromTrimmed = DashboardScreens.CityForecast.fromCityName("  Barcelona  ")
        assertEquals("Barcelona", fromTrimmed.cityName)
        assertTrue(fromTrimmed.isFavoriteDetail)

        val navOptions = AppShellState.NAV_OPTIONS
        assertEquals(2, navOptions.size)
        assertEquals(R.string.city_tab, navOptions[0].titleRes)
        assertEquals(DashboardScreens.CityForecast(), navOptions[0].route)
        assertEquals(R.string.favorite_tab, navOptions[1].titleRes)
        assertEquals(DashboardScreens.Favorites, navOptions[1].route)
        assertEquals(navOptions.map { it.route }.distinct().size, navOptions.size)

        assertEquals(
            "cityName",
            DashboardScreens.CityForecast.serializer().descriptor.getElementName(0),
        )
    }
}
