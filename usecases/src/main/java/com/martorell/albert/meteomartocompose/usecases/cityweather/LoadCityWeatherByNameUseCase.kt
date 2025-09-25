package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import javax.inject.Inject

class LoadCityWeatherByNameUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke(
        cityName: String
    ): ResultResponse<CityWeatherDomain> =
        cityWeatherRepository.loadCityByName(cityName)

}