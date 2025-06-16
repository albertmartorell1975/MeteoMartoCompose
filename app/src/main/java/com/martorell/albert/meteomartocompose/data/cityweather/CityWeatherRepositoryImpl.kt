package com.martorell.albert.meteomartocompose.data.cityweather

import arrow.core.right
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.customTryCatch
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain

class CityWeatherRepositoryImpl(
    private val cityWeatherServerDataSource: CityWeatherServerDataSource,
    private val cityWeatherLocalDataSource: CityWeatherLocalDataSource
) :
    CityWeatherRepository {

    //val city = cityWeatherLocalDataSource.loadCity()

    override suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String
    ): ResultResponse<CityWeatherDomain> =

        customTryCatch {
            val cityServer = cityWeatherServerDataSource.getWeather(
                lat = latitude,
                lon = longitude
            )

            cityWeatherLocalDataSource.addCity(cityServer)
            return cityWeatherLocalDataSource.loadCity().right()

        }

}