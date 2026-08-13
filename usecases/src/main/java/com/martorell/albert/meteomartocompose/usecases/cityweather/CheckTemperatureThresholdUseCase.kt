package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class CheckTemperatureThresholdUseCase @Inject constructor(
    private val cityWeatherRepository: CityWeatherRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) {
    operator fun invoke(): Flow<TemperatureAlertResult> =
        cityWeatherRepository.listOfCities
            .mapNotNull { cities -> cities.firstOrNull { it.justAdded } }
            .combine(remoteConfigRepository.getTemperatureThreshold()) { city, threshold ->
                TemperatureAlertResult(
                    showAlert = city.temperature > threshold,
                    currentTemperature = city.temperature,
                    threshold = threshold
                )
            }
            .distinctUntilChanged()
}
