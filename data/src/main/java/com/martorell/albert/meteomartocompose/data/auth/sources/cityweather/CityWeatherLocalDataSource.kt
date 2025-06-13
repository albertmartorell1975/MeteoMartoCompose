package com.martorell.albert.meteomartocompose.data.auth.sources.cityweather

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse

interface CityWeatherLocalDataSource {

    suspend fun addCity(cityServer: CityWeatherResponse)
    suspend fun loadCity(): CityWeatherDomain

}