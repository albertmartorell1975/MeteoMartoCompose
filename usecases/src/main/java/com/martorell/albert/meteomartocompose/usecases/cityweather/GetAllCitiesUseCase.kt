package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCitiesUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    fun invoke(): Flow<List<CityWeatherDomain>> =
        cityWeatherRepository.listOfCities

}