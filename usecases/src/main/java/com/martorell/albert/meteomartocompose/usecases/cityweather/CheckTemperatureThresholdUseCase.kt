package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class CheckTemperatureThresholdUseCase @Inject constructor(
    private val cityWeatherRepository: CityWeatherRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) {
    operator fun invoke(): Flow<TemperatureAlertResult> =
        cityWeatherRepository.listOfCities
            .combine(remoteConfigRepository.getTemperatureThreshold()) { cities, threshold ->
                val city = cities.firstOrNull { it.justAdded }
                TemperatureAlertResult(
                    showAlert = city?.let { it.temperature > threshold } ?: false,
                    currentTemperature = city?.temperature ?: 0.0,
                    threshold = threshold
                )
            }
            .distinctUntilChanged()
}
