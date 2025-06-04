package com.martorell.albert.meteomartocompose.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

sealed class SubGraphs {

    @Serializable
    data object Auth : SubGraphs()

    @Serializable
    data object Dashboard : SubGraphs()

}

sealed class AuthScreens {

    @Serializable
    data object Login : AuthScreens()

    @Serializable
    data object Terms : AuthScreens()

    @Serializable
    data object SignUp : AuthScreens()
}

sealed class DashboardScreens {

    @Serializable
    data object Home : DashboardScreens()
    @Serializable
    data object CityWeather : DashboardScreens()
    @Serializable
    data object Favorites : DashboardScreens()
    @Serializable
    data object WeatherWarnings : DashboardScreens()
}

fun NavHostController.navigatePoppingUpToStartDestination(route: Any) {

    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }

}