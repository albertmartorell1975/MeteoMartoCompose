package com.martorell.albert.meteomartocompose.data.auth.sources.cityweather

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import kotlinx.coroutines.flow.Flow

interface CityWeatherLocalDataSource {

    suspend fun isEmpty():Boolean
    suspend fun addCity(cityServer: CityWeatherResponse)
    suspend fun loadCity(name: String): CityWeatherDomain
    suspend fun updateFavorite(cityWeatherDomain: CityWeatherDomain)
    suspend fun makeAllCitiesAsNotJustAdded()
    fun getAll(): Flow<List<CityWeatherDomain>>

}