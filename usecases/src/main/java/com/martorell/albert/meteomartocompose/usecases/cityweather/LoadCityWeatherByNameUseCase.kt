package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import javax.inject.Inject

class LoadCityWeatherByNameUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend operator fun invoke(cityName: String): Result<CityWeatherDomain, DomainError> =
        cityWeatherRepository.loadCityByName(cityName)

}