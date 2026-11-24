package com.martorell.albert.meteomartocompose.domain.cityweather

/**
 * Domain representation of a city's weather telemetry and user-specific alert/favorite state.
 *
 * @property name City display name serving as the unique domain identifier.
 * @property justAdded Whether the city was recently added and awaits initial acknowledgement.
 * @property weatherDescription Textual summary of the current weather condition, if available.
 * @property weatherIcon Full URL to the OpenWeather icon asset, if available.
 * @property weatherConditionId Raw OpenWeather numeric condition code (`0` when unknown).
 * @property isNight Whether the telemetry corresponds to nocturnal conditions at the city location.
 * @property temperature Current temperature value.
 * @property temperatureMin Minimum temperature value.
 * @property temperatureMax Maximum temperature value.
 * @property rain Precipitation volume, if reported.
 * @property pressure Atmospheric pressure value.
 * @property latitude Geographic latitude of the city.
 * @property longitude Geographic longitude of the city.
 * @property favorite Whether the user has marked this city as a favorite.
 * @property isAlertNotified Whether a high-temperature alert has already been notified.
 */
data class CityWeatherDomain(
    val name: String = "",
    val justAdded: Boolean = false,
    val weatherDescription: String? = "",
    val weatherIcon: String? = "",
    val weatherConditionId: Int = 0,
    val isNight: Boolean = false,
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val rain: Double? = 0.0,
    val pressure: Int,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val favorite: Boolean = false,
    val isAlertNotified: Boolean = false,
) {
    /**
     * Semantic [WeatherCondition] derived deterministically from [weatherConditionId]
     * so classification stays consistent with the persisted condition identifier.
     */
    val weatherCondition: WeatherCondition
        get() = WeatherCondition.fromOpenWeatherId(weatherConditionId)

    companion object {
        /** Default empty sentinel instance used when no weather record is available. */
        val EMPTY = CityWeatherDomain(
            temperature = 0.0,
            temperatureMin = 0.0,
            temperatureMax = 0.0,
            pressure = 0,
        )
    }
}