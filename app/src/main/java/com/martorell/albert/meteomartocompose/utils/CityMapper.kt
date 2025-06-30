package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather

fun CityWeatherResponse.toRoom(): CityWeather =
    CityWeather(
        name = this.name,
        justAdded = true,
        weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else "",
        weatherIcon = if (this.weather.isNotEmpty()) this.weather[0].icon else "",
        temperature = this.main.temp.openWeatherConverter(),
        temperatureMin = this.main.temp_min.openWeatherConverter(),
        temperatureMax = this.main.temp_max.openWeatherConverter(),
        rain = if (this.rain != null) this.rain?.quantity else 0.0,
        pressure = this.main.pressure,
        latitude = this.coord.lat,
        longitude = this.coord.lon
    )


fun CityWeather.toDomain(): CityWeatherDomain =
    CityWeatherDomain(
        name = this.name,
        justAdded = this.justAdded,
        weatherDescription = this.weatherDescription,
        weatherIcon = "https://openweathermap.org/img/wn/${this.weatherIcon}@2x.png",
        temperature = this.temperature,
        temperatureMin = this.temperatureMin,
        temperatureMax = this.temperatureMax,
        rain = this.rain,
        pressure = this.pressure,
        favorite = this.favorite,
        latitude = this.latitude,
        longitude = this.longitude
    )

fun CityWeatherDomain.toRoom(): CityWeather =

    CityWeather(
        name = this.name,
        justAdded = this.justAdded,
        weatherDescription = this.weatherDescription,
        weatherIcon = "https://openweathermap.org/img/wn/${this.weatherIcon}@2x.png",
        temperature = this.temperature,
        temperatureMin = this.temperatureMin,
        temperatureMax = this.temperatureMax,
        rain = this.rain,
        pressure = this.pressure,
        favorite = this.favorite,
        latitude = this.latitude,
        longitude = this.longitude
    )

fun List<CityWeather>.listToDomain(): List<CityWeatherDomain> {

    val myList = mutableListOf<CityWeatherDomain>()

    for (city in this) {
        val cityDomain = CityWeatherDomain(
            name = city.name,
            justAdded = city.justAdded,
            weatherDescription = city.weatherDescription,
            weatherIcon = "https://openweathermap.org/img/wn/${city.weatherIcon}@2x.png",
            temperature = city.temperature,
            temperatureMin = city.temperatureMin,
            temperatureMax = city.temperatureMax,
            rain = city.rain,
            pressure = city.pressure,
            favorite = city.favorite,
            latitude = city.latitude,
            longitude = city.longitude
        )

        myList.add(cityDomain)
    }

    return myList

}
