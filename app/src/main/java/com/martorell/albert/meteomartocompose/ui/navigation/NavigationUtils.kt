package com.martorell.albert.meteomartocompose.ui.navigation

import kotlinx.serialization.Serializable

sealed class SubGraphs {

    @Serializable
    data object Auth : SubGraphs()

    @Serializable
    data object Dashboard : SubGraphs()

}

sealed class AuthScreens {

    @Serializable
    data object Login: AuthScreens()
    @Serializable
    data object Terms: AuthScreens()
}

sealed class DashboardScreens{

    @Serializable
    data object CityWeather: DashboardScreens()
}