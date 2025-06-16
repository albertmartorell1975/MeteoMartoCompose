package com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain

interface CityWeatherRepository {

    suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String
    ): ResultResponse<CityWeatherDomain>

}