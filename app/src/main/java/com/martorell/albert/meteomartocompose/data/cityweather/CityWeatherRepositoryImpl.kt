package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.customFlowTryCatch
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CityWeatherRepositoryImpl @Inject constructor(
    private val cityWeatherServerDataSource: CityWeatherServerDataSource,
    private val cityWeatherLocalDataSource: CityWeatherLocalDataSource,
) :
    CityWeatherRepository {

    override val listOfCities: Flow<List<CityWeatherDomain>>
        get() =
            cityWeatherLocalDataSource.getAll()

    override suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String
    ): CustomErrorFlow? = customFlowTryCatch {

        val cityServer = cityWeatherServerDataSource.getWeather(
            lat = latitude,
            lon = longitude
        )

        cityWeatherLocalDataSource.refreshCity(cityServer)
    }

    override suspend fun switchFavorite(city: CityWeatherDomain) {

        val updatedCity = city.copy(favorite = !city.favorite)
        cityWeatherLocalDataSource.updateFavorite(updatedCity)

    }

    override suspend fun isCurrentCityFavorite(): Boolean =
        cityWeatherLocalDataSource.isCurrentCityFavorite()

    override suspend fun removeCityAsFavorite(cityName: String) {

        cityWeatherLocalDataSource.removeCityAsFavorite(cityName)

    }

    override suspend fun loadCityByName(cityName: String): ResultResponse<CityWeatherDomain> =
        cityWeatherLocalDataSource.loadCity(cityName)

    override suspend fun updateAlertStatus(name: String, isNotified: Boolean) {
        cityWeatherLocalDataSource.updateAlertStatus(name, isNotified)
    }

}