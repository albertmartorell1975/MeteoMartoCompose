package com.martorell.albert.meteomartocompose.utils

import com.martorell.albert.meteomartocompose.BuildConfig
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.WeatherCondition
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Clouds
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Coord
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Main
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Rain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Sys
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Weather
import com.martorell.albert.meteomartocompose.domain.cityweather.response.Wind
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CityMapperTest {

    @Test
    fun `resolveIsNight prioritizes icon day and night suffixes and falls back to sunrise sunset timestamps`() {
        val sunrise = 1_700_020_000
        val midday = 1_700_040_000
        val sunset = 1_700_060_000
        val afterSunset = 1_700_070_000
        val beforeSunrise = 1_700_010_000

        assertTrue(resolveIsNight(iconCode = "01n", dt = midday, sunrise = sunrise, sunset = sunset))
        assertTrue(resolveIsNight(iconCode = " 02N ", dt = midday, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = "10d", dt = afterSunset, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = " 01D ", dt = beforeSunrise, sunrise = sunrise, sunset = sunset))

        assertTrue(resolveIsNight(iconCode = null, dt = beforeSunrise, sunrise = sunrise, sunset = sunset))
        assertTrue(resolveIsNight(iconCode = "", dt = afterSunset, sunrise = sunrise, sunset = sunset))
        assertTrue(resolveIsNight(iconCode = "   ", dt = afterSunset, sunrise = sunrise, sunset = sunset))
        assertTrue(resolveIsNight(iconCode = "01x", dt = beforeSunrise, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = null, dt = midday, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = "", dt = sunrise, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = "", dt = sunset, sunrise = sunrise, sunset = sunset))

        assertFalse(resolveIsNight(iconCode = null, dt = 0, sunrise = sunrise, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = null, dt = midday, sunrise = 0, sunset = sunset))
        assertFalse(resolveIsNight(iconCode = null, dt = midday, sunrise = sunrise, sunset = 0))
        assertFalse(resolveIsNight(iconCode = null, dt = afterSunset, sunrise = sunset, sunset = sunrise))
    }

    @Test
    fun `toRoom and toDomain preserve weatherConditionId isNight and fallback safely on empty weather list`() {
        val rainResponse = buildResponse(
            weather = listOf(Weather(description = "light rain", icon = "10n", id = 500, main = "Rain")),
            dt = 1_700_040_000,
            sunrise = 1_700_020_000,
            sunset = 1_700_060_000,
            rain = Rain(quantity = 3.5),
        )

        val entity = rainResponse.toRoom()
        assertEquals(500, entity.weatherConditionId)
        assertTrue(entity.isNight)
        assertEquals("light rain", entity.weatherDescription)
        assertEquals("${BuildConfig.OPEN_WEATHER_ICON_URL}10n@2x.png", entity.weatherIcon)
        assertEquals(3.5, entity.rain)

        val domain = entity.toDomain()
        assertEquals(500, domain.weatherConditionId)
        assertTrue(domain.isNight)
        assertEquals(WeatherCondition.RAIN, domain.weatherCondition)

        val favoriteRoundTrip = domain.copy(favorite = true, isAlertNotified = true).toRoom()
        assertEquals(500, favoriteRoundTrip.weatherConditionId)
        assertTrue(favoriteRoundTrip.isNight)
        assertTrue(favoriteRoundTrip.favorite)
        assertTrue(favoriteRoundTrip.isAlertNotified)

        val listed = listOf(entity).listToDomain()
        assertEquals(1, listed.size)
        assertEquals(500, listed.first().weatherConditionId)
        assertTrue(listed.first().isNight)

        val emptyWeatherResponse = buildResponse(
            weather = emptyList(),
            dt = 1_700_070_000,
            sunrise = 1_700_020_000,
            sunset = 1_700_060_000,
            rain = null,
        )
        val fallbackEntity = emptyWeatherResponse.toRoom()
        assertEquals(0, fallbackEntity.weatherConditionId)
        assertEquals("", fallbackEntity.weatherDescription)
        assertEquals("", fallbackEntity.weatherIcon)
        assertTrue(fallbackEntity.isNight)
        assertEquals(0.0, fallbackEntity.rain)
        assertEquals(WeatherCondition.BASELINE, fallbackEntity.toDomain().weatherCondition)

        val nullEntity: CityWeather? = null
        val emptyDomain = nullEntity.toDomain()
        assertEquals(CityWeatherDomain.EMPTY, emptyDomain)
        assertEquals(0, emptyDomain.weatherConditionId)
        assertFalse(emptyDomain.isNight)
        assertEquals(WeatherCondition.BASELINE, emptyDomain.weatherCondition)
    }

    private fun buildResponse(
        weather: List<Weather>,
        dt: Int,
        sunrise: Int,
        sunset: Int,
        rain: Rain?,
    ): CityWeatherResponse = CityWeatherResponse(
        base = "stations",
        clouds = Clouds(all = 40),
        cod = 200,
        coord = Coord(lat = 41.39, lon = 2.17),
        dt = dt,
        id = 3128760,
        main = Main(
            feels_like = 210.0,
            grnd_level = 1010,
            humidity = 70,
            pressure = 1015,
            sea_level = 1015,
            temp = 215.0,
            temp_max = 240.0,
            temp_min = 180.0,
        ),
        name = "Barcelona",
        rain = rain,
        sys = Sys(country = "ES", id = 1, sunrise = sunrise, sunset = sunset, type = 1),
        timezone = 3600,
        visibility = 10000,
        weather = weather,
        wind = Wind(deg = 120, gust = 5.0, speed = 3.2),
    )
}
