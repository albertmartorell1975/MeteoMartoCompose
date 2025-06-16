package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import javax.inject.Inject

class LoadCityWeatherUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke(
        latitude: String,
        longitude: String
    ): ResultResponse<CityWeatherDomain> {
        return cityWeatherRepository.loadCityCurrentWeather(
            latitude = latitude,
            longitude = longitude
        )
    }

}