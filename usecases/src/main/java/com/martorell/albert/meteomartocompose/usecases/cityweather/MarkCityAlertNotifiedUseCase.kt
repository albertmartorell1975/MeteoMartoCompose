package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import javax.inject.Inject

class MarkCityAlertNotifiedUseCase @Inject constructor(
    private val cityWeatherRepository: CityWeatherRepository,
) {
    suspend operator fun invoke(cityName: String, notified: Boolean) {
        cityWeatherRepository.updateAlertStatus(cityName, notified)
    }
}
