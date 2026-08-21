package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.BuildConfig
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather

fun CityWeatherResponse.toRoom(): CityWeather =
    CityWeather(
        name = this.name,
        justAdded = true,
        favorite = false,
        weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else "",
        weatherIcon = if (this.weather.isNotEmpty()) "${BuildConfig.OPEN_WEATHER_ICON_URL}${this.weather[0].icon}@2x.png" else "",
        temperature = this.main.temp.openWeatherConverter(),
        temperatureMin = this.main.temp_min.openWeatherConverter(),
        temperatureMax = this.main.temp_max.openWeatherConverter(),
        rain = if (this.rain != null) this.rain?.quantity else 0.0,
        pressure = this.main.pressure,
        latitude = this.coord.lat,
        longitude = this.coord.lon,
        isAlertNotified = false,
    )


fun CityWeather?.toDomain(): CityWeatherDomain =
    this?.let {
        CityWeatherDomain(
            name = this.name,
            justAdded = this.justAdded,
            weatherDescription = this.weatherDescription,
            weatherIcon = this.weatherIcon,
            temperature = this.temperature,
            temperatureMin = this.temperatureMin,
            temperatureMax = this.temperatureMax,
            rain = this.rain,
            pressure = this.pressure,
            favorite = this.favorite,
            latitude = this.latitude,
            longitude = this.longitude,
            isAlertNotified = this.isAlertNotified,
        )
    } ?: CityWeatherDomain.EMPTY

fun CityWeatherDomain.toRoom(): CityWeather =

    CityWeather(
        name = this.name,
        justAdded = this.justAdded,
        weatherDescription = this.weatherDescription,
        weatherIcon = this.weatherIcon,
        temperature = this.temperature,
        temperatureMin = this.temperatureMin,
        temperatureMax = this.temperatureMax,
        rain = this.rain,
        pressure = this.pressure,
        favorite = this.favorite,
        latitude = this.latitude,
        longitude = this.longitude,
        isAlertNotified = this.isAlertNotified,
    )

fun List<CityWeather>.listToDomain(): List<CityWeatherDomain> {

    val myList = mutableListOf<CityWeatherDomain>()

    for (city in this) {
        val cityDomain = CityWeatherDomain(
            name = city.name,
            justAdded = city.justAdded,
            weatherDescription = city.weatherDescription,
            weatherIcon = city.weatherIcon,
            temperature = city.temperature,
            temperatureMin = city.temperatureMin,
            temperatureMax = city.temperatureMax,
            rain = city.rain,
            pressure = city.pressure,
            favorite = city.favorite,
            latitude = city.latitude,
            longitude = city.longitude,
            isAlertNotified = city.isAlertNotified,
        )

        myList.add(cityDomain)
    }

    return myList

}
