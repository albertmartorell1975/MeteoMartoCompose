package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import javax.inject.Inject

class IsCurrentCityFavoriteUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke():Boolean {

        return cityWeatherRepository.isCurrentCityFavorite()

    }

}