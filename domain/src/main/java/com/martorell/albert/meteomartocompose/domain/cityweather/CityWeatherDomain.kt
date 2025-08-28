package com.martorell.albert.meteomartocompose.domain.cityweather

data class CityWeatherDomain(
    val name: String = "",
    val justAdded: Boolean = false,
    val weatherDescription: String? = "",
    val weatherIcon: String? = "",
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val rain: Double? = 0.0,
    val pressure: Int,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val favorite: Boolean = false
)