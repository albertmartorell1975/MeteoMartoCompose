package com.martorell.albert.meteomartocompose.data.city.repositories

import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import kotlinx.coroutines.flow.Flow

interface CityWeatherRepository {

    val listOfCities: Flow<List<CityWeatherDomain>>

    suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String
    ): CustomErrorFlow?

    suspend fun switchFavorite(city: CityWeatherDomain)

    suspend fun isCurrentCityFavorite(): Boolean

    suspend fun removeCityAsFavorite(cityName: String)

    suspend fun loadCityByName(cityName: String): ResultResponse<CityWeatherDomain>

}