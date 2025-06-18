package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather

fun CityWeatherResponse.toRoom(): CityWeather =
    CityWeather(
        name = this.name,
        weatherDescription = if (this.weather.isNotEmpty()) this.weather[0].description else "",
        weatherIcon = if (this.weather.isNotEmpty()) this.weather[0].icon else "",
        temperature = this.main.temp.openWeatherConverter(),
        temperatureMin = this.main.temp_min.openWeatherConverter(),
        temperatureMax = this.main.temp_max.openWeatherConverter(),
        rain = if (this.rain != null) this.rain?.quantity else 0.0,
        pressure = this.main.pressure
    )


fun CityWeather.toDomain(): CityWeatherDomain =
    CityWeatherDomain(
        name = this.name,
        weatherDescription = this.weatherDescription,
        weatherIcon = "https://openweathermap.org/img/wn/${this.weatherIcon}@2x.png",
        temperature = this.temperature,
        temperatureMin = this.temperatureMin,
        temperatureMax = this.temperatureMax,
        rain = this.rain,
        pressure = this.pressure,
        favorite = this.favorite
    )