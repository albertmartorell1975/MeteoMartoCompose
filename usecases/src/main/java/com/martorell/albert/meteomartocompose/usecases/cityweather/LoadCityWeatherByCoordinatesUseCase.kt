package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import javax.inject.Inject

class LoadCityWeatherByCoordinatesUseCase @Inject constructor(private val cityWeatherRepository: CityWeatherRepository) {

    suspend operator fun invoke(
        latitude: String,
        longitude: String
    ): Result<Unit, DomainError> =

        cityWeatherRepository.loadCityCurrentWeather(
            latitude = latitude,
            longitude = longitude
        )

}