package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

/** Top-level navigation sub-graphs partitioning unauthenticated and authenticated flows. */
sealed class SubGraphs {

    @Serializable
    data object Auth : SubGraphs()

    @Serializable
    data object Dashboard : SubGraphs()
}

/** Type-safe destinations within the unauthenticated [SubGraphs.Auth] flow. */
sealed class AuthScreens {

    @Serializable
    data object Login : AuthScreens()

    @Serializable
    data object Terms : AuthScreens()

    @Serializable
    data object SignUp : AuthScreens()
}

/** Type-safe destinations within the authenticated [SubGraphs.Dashboard] flow. */
sealed class DashboardScreens {

    /**
     * Unified forecast route serving current-location weather (`cityName == null`) and
     * favorite-city detail (`cityName != null`). The `cityName` argument matches the
     * `SavedStateHandle` key preserved across process death.
     */
    @Serializable
    data class CityForecast(
        val cityName: String? = null,
    ) : DashboardScreens() {

        val isFavoriteDetail: Boolean
            get() = !cityName.isNullOrBlank()

        companion object {
            fun fromCityName(cityName: String?): CityForecast =
                CityForecast(cityName = cityName?.trim()?.takeIf { it.isNotEmpty() })
        }
    }

    @Serializable
    data object Favorites : DashboardScreens()

    @Serializable
    data class HighTemperatureAlert(
        val temperature: Double,
    ) : DashboardScreens()
}

/**
 * Navigates to [route] as a single-top destination while saving and restoring tab state up to [T].
 * Parameterizing [T] preserves tab switching even after [SubGraphs.Auth] is popped as the graph start destination.
 */
inline fun <reified T : Any> NavHostController.navigatePoppingUpToStartDestination(route: Any) {
    navigate(route) {
        popUpTo<T> {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
