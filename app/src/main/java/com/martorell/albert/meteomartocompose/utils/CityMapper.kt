package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather

fun CityWeatherResponse.toRoom(): CityWeather =
    CityWeather(
        name = this.name,
        weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else "",
        weatherIcon = if (this.weather.isNotEmpty()) this.weather[0].icon else "",
        temperature = this.main.temp,
        temperatureMin = this.main.temp_min,
        temperatureMax = this.main.temp_max,
        rain = this.rain?.quantity,
        pressure = this.main.pressure
    )


fun CityWeather.toDomain(): CityWeatherDomain =
    CityWeatherDomain(
        name = this.name,
        weatherDescription = this.weatherDescription,
        weatherIcon = this.weatherIcon,
        temperature = this.temperature,
        temperatureMin = this.temperatureMin,
        temperatureMax = this.temperatureMax,
        rain = this.rain,
        pressure = this.pressure
    )