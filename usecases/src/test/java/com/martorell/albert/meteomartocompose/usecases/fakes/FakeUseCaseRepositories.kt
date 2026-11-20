package com.martorell.albert.meteomartocompose.usecases.fakes

import arrow.core.left
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.city.repositories.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCityWeatherRepository(
    initialCities: List<CityWeatherDomain> = emptyList(),
) : CityWeatherRepository {

    private val citiesFlow = MutableStateFlow(initialCities)

    override val listOfCities: Flow<List<CityWeatherDomain>>
        get() = citiesFlow

    fun setCities(cities: List<CityWeatherDomain>) {
        citiesFlow.value = cities
    }

    override suspend fun loadCityCurrentWeather(
        latitude: String,
        longitude: String,
    ): CustomErrorFlow? = null

    override suspend fun switchFavorite(city: CityWeatherDomain) = Unit

    override suspend fun isCurrentCityFavorite(): Boolean = false

    override suspend fun removeCityAsFavorite(cityName: String) = Unit

    override suspend fun loadCityByName(cityName: String): ResultResponse<CityWeatherDomain> =
        CustomError.Unknown("Not configured").left()

    override suspend fun updateAlertStatus(name: String, isNotified: Boolean) {
        citiesFlow.value = citiesFlow.value.map { existing ->
            if (existing.name == name) existing.copy(isAlertNotified = isNotified) else existing
        }
    }
}

class FakeRemoteConfigRepository(
    initialThreshold: Double = 30.0,
    initialInterval: Long = 60L,
) : RemoteConfigRepository {

    val thresholdFlow = MutableStateFlow(initialThreshold)
    val intervalFlow = MutableStateFlow(initialInterval)

    override fun getTemperatureThreshold(): Flow<Double> = thresholdFlow

    override fun getWeatherCheckInterval(): Flow<Long> = intervalFlow
}
