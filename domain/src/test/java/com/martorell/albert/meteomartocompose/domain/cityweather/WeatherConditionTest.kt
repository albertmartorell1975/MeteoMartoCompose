package com.martorell.albert.meteomartocompose.domain.cityweather

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherConditionTest {

    @Test
    fun `fromOpenWeatherId maps clear precipitation and fallback boundary codes deterministically`() {
        assertEquals(WeatherCondition.SUNNY, WeatherCondition.fromOpenWeatherId(800))

        val rainBoundaryCodes = listOf(
            200,
            232,
            300,
            321,
            500,
            531,
            600,
            622,
        )
        rainBoundaryCodes.forEach { code ->
            assertEquals("Expected RAIN for code $code", WeatherCondition.RAIN, WeatherCondition.fromOpenWeatherId(code))
        }

        val baselineBoundaryCodes = listOf(
            -1,
            0,
            199,
            233,
            299,
            322,
            499,
            532,
            599,
            623,
            700,
            781,
            799,
            801,
            804,
            999,
        )
        baselineBoundaryCodes.forEach { code ->
            assertEquals(
                "Expected BASELINE for code $code",
                WeatherCondition.BASELINE,
                WeatherCondition.fromOpenWeatherId(code),
            )
        }

        assertEquals(WeatherCondition.BASELINE, CityWeatherDomain.EMPTY.weatherCondition)
        assertEquals(
            WeatherCondition.SUNNY,
            CityWeatherDomain.EMPTY.copy(weatherConditionId = 800).weatherCondition,
        )
        assertEquals(
            WeatherCondition.RAIN,
            CityWeatherDomain.EMPTY.copy(weatherConditionId = 500).weatherCondition,
        )
    }
}
