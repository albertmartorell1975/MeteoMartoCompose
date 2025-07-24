package com.martorell.albert.meteomartocompose.usecases.favorites

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import javax.inject.Inject

class RemoveCityAsFavoriteUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke(cityName: String) {

        cityWeatherRepository.removeCityAsFavorite(cityName)

    }

}