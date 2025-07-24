package com.martorell.albert.meteomartocompose.data.auth.sources.cityweather

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import kotlinx.coroutines.flow.Flow

interface CityWeatherLocalDataSource {

    suspend fun isEmpty(): Boolean
    suspend fun addCity(cityServer: CityWeatherResponse)
    suspend fun updateCity(
        cityName: String,
        weatherDescription: String?,
        weatherIcon: String?,
        pressure: Int,
        temperatureMax: Double,
        temperatureMin: Double,
        temperature: Double
    )

    suspend fun loadCity(name: String): CityWeatherDomain
    suspend fun updateFavorite(cityWeatherDomain: CityWeatherDomain)
    suspend fun makeAllCitiesAsNotJustAdded()
    fun getAll(): Flow<List<CityWeatherDomain>>
    suspend fun isCurrentCityFavorite(): Boolean
    suspend fun removeCityAsFavorite(cityName: String)

}