package com.martorell.albert.meteomartocompose.usecases.favorites

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveCityAsFavoriteUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    fun invoke(cityName:String): Flow<List<CityWeatherDomain>> =
        cityWeatherRepository.listOfCities

}