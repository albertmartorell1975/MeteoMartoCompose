package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.city.sources.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.data.util.safeCall
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CityWeatherRepositoryImpl @Inject constructor(
    private val cityWeatherServerDataSource: CityWeatherServerDataSource,
    private val cityWeatherLocalDataSource: CityWeatherLocalDataSource,
    private val errorMapper: ErrorMapper,
) : CityWeatherRepository {

    override val listOfCities: Flow<List<CityWeatherDomain>>
        get() = cityWeatherLocalDataSource.getAll()

    override suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String
    ): Result<Unit, DomainError> = safeCall(mapper = errorMapper::map) {
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

    override suspend fun loadCityByName(cityName: String): Result<CityWeatherDomain, DomainError> =
        cityWeatherLocalDataSource.loadCity(cityName)

    override suspend fun updateAlertStatus(name: String, isNotified: Boolean) {
        cityWeatherLocalDataSource.updateAlertStatus(name, isNotified)
    }
}
