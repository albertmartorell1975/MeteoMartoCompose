package com.martorell.albert.meteomartocompose.domain.cityweather.response

data class CityWeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coordinates: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain?,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)