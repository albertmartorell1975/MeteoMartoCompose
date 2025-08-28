package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import javax.inject.Inject

class LoadCityWeatherByCoordinatesUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke(
        latitude: String,
        longitude: String
    ): CustomErrorFlow? =

        cityWeatherRepository.loadCityCurrentWeather(
            latitude = latitude,
            longitude = longitude
        )

}