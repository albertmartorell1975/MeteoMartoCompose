package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import javax.inject.Inject

class SwitchFavoriteUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend fun invoke(
        city: CityWeatherDomain
    ) {

        cityWeatherRepository.switchFavorite(city)

    }

}