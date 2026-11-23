package com.martorell.albert.meteomartocompose.data.city.repositories

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface CityWeatherRepository {

    val listOfCities: Flow<List<CityWeatherDomain>>

    suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String,
    ): Result<Unit, DomainError>

    suspend fun switchFavorite(city: CityWeatherDomain)

    suspend fun isCurrentCityFavorite(): Boolean

    suspend fun removeCityAsFavorite(cityName: String)

    suspend fun loadCityByName(cityName: String): Result<CityWeatherDomain, DomainError>

    suspend fun updateAlertStatus(name: String, isNotified: Boolean)

}