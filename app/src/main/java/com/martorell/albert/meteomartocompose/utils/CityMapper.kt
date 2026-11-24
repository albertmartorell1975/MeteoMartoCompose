package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.BuildConfig
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather

private const val NIGHT_ICON_SUFFIX = "n"
private const val DAY_ICON_SUFFIX = "d"
private const val UNKNOWN_CONDITION_ID = 0

/**
 * Deterministically derives whether weather telemetry represents nocturnal conditions.
 *
 * Prioritizes the raw OpenWeather [iconCode] suffix (`"n"` -> `true`, `"d"` -> `false`,
 * case-insensitive and trimmed) before URL construction. When [iconCode] is null, blank,
 * or lacks a day/night suffix, falls back to checking whether [dt] falls outside the
 * `[sunrise, sunset]` daylight window (when all timestamps are positive and `sunrise < sunset`),
 * defaulting safely to `false` otherwise.
 *
 * @param iconCode Raw OpenWeather icon code (e.g., `"01n"` or `"10d"`), if present.
 * @param dt Observation timestamp in Unix seconds.
 * @param sunrise Location sunrise timestamp in Unix seconds.
 * @param sunset Location sunset timestamp in Unix seconds.
 * @return `true` if nocturnal conditions are detected, `false` otherwise.
 */
fun resolveIsNight(
    iconCode: String?,
    dt: Int,
    sunrise: Int,
    sunset: Int,
): Boolean {
    val normalizedIcon = iconCode?.trim()?.lowercase()
    return when {
        normalizedIcon.isNullOrEmpty() -> isOutsideDaylightWindow(dt, sunrise, sunset)
        normalizedIcon.endsWith(NIGHT_ICON_SUFFIX) -> true
        normalizedIcon.endsWith(DAY_ICON_SUFFIX) -> false
        else -> isOutsideDaylightWindow(dt, sunrise, sunset)
    }
}

private fun isOutsideDaylightWindow(
    dt: Int,
    sunrise: Int,
    sunset: Int,
): Boolean = dt > 0 && sunrise > 0 && sunset > 0 && sunrise < sunset &&
    (dt < sunrise || dt > sunset)

/**
 * Maps an OpenWeather [CityWeatherResponse] network DTO into a Room [CityWeather] entity.
 *
 * Extracts `weatherConditionId` and derives `isNight` via [resolveIsNight] from the primary
 * weather entry before constructing the icon URL, defaulting safely when `weather` is empty.
 */
fun CityWeatherResponse.toRoom(): CityWeather {
    val primaryWeather = weather.firstOrNull()
    return CityWeather(
        name = name,
        justAdded = true,
        favorite = false,
        weatherDescription = primaryWeather?.description.orEmpty(),
        weatherIcon = primaryWeather?.icon?.takeIf { it.isNotBlank() }?.let {
            "${BuildConfig.OPEN_WEATHER_ICON_URL}$it@2x.png"
        }.orEmpty(),
        weatherConditionId = primaryWeather?.id ?: UNKNOWN_CONDITION_ID,
        isNight = resolveIsNight(
            iconCode = primaryWeather?.icon,
            dt = dt,
            sunrise = sys.sunrise,
            sunset = sys.sunset,
        ),
        temperature = main.temp.openWeatherConverter(),
        temperatureMin = main.temp_min.openWeatherConverter(),
        temperatureMax = main.temp_max.openWeatherConverter(),
        rain = rain?.quantity ?: 0.0,
        pressure = main.pressure,
        latitude = coord.lat,
        longitude = coord.lon,
        isAlertNotified = false,
    )
}

/**
 * Maps a nullable Room [CityWeather] entity to a [CityWeatherDomain] model.
 *
 * Returns [CityWeatherDomain.EMPTY] when the receiver is `null`.
 */
fun CityWeather?.toDomain(): CityWeatherDomain =
    this?.let { city ->
        CityWeatherDomain(
            name = city.name,
            justAdded = city.justAdded,
            weatherDescription = city.weatherDescription,
            weatherIcon = city.weatherIcon,
            weatherConditionId = city.weatherConditionId,
            isNight = city.isNight,
            temperature = city.temperature,
            temperatureMin = city.temperatureMin,
            temperatureMax = city.temperatureMax,
            rain = city.rain,
            pressure = city.pressure,
            favorite = city.favorite,
            latitude = city.latitude,
            longitude = city.longitude,
            isAlertNotified = city.isAlertNotified,
        )
    } ?: CityWeatherDomain.EMPTY

/**
 * Maps a [CityWeatherDomain] model back into a Room [CityWeather] entity for persistence.
 */
fun CityWeatherDomain.toRoom(): CityWeather =
    CityWeather(
        name = name,
        justAdded = justAdded,
        weatherDescription = weatherDescription,
        weatherIcon = weatherIcon,
        weatherConditionId = weatherConditionId,
        isNight = isNight,
        temperature = temperature,
        temperatureMin = temperatureMin,
        temperatureMax = temperatureMax,
        rain = rain,
        pressure = pressure,
        favorite = favorite,
        latitude = latitude,
        longitude = longitude,
        isAlertNotified = isAlertNotified,
    )

/**
 * Maps a list of Room [CityWeather] entities to their corresponding [CityWeatherDomain] models.
 */
fun List<CityWeather>.listToDomain(): List<CityWeatherDomain> = map { it.toDomain() }
