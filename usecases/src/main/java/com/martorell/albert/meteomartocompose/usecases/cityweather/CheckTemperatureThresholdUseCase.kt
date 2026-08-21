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
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    operator fun invoke(): Flow<TemperatureAlertResult> =
        cityWeatherRepository.listOfCities
            .combine(remoteConfigRepository.getTemperatureThreshold()) { cities, threshold ->
                // If there is no city marked as 'justAdded', return the empty object 
                // and stop the execution of this specific emission of the combine block.
                val city = cities.firstOrNull { it.justAdded } ?: return@combine TemperatureAlertResult.EMPTY
                val isAboveThreshold = city.temperature >= threshold

                TemperatureAlertResult(
                    cityName = city.name,
                    showAlert = isAboveThreshold && !city.isAlertNotified,
                    isPersistentAlertActive = isAboveThreshold,
                    currentTemperature = city.temperature,
                    threshold = threshold,
                )
            }
            .distinctUntilChanged()
}
