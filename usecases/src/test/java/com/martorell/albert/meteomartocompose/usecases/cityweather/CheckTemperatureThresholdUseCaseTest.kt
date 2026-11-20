package com.martorell.albert.meteomartocompose.usecases.cityweather

import app.cash.turbine.test
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.TemperatureAlertResult
import com.martorell.albert.meteomartocompose.usecases.fakes.FakeCityWeatherRepository
import com.martorell.albert.meteomartocompose.usecases.fakes.FakeRemoteConfigRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckTemperatureThresholdUseCaseTest {

    private val cityWeatherRepository = FakeCityWeatherRepository()
    private val remoteConfigRepository = FakeRemoteConfigRepository(initialThreshold = 30.0)
    private val useCase = CheckTemperatureThresholdUseCase(cityWeatherRepository, remoteConfigRepository)

    @Test
    fun `when temperature meets or exceeds threshold emits active alert and suppresses showAlert once notified`() =
        runTest {
            val equalThresholdCity = CityWeatherDomain(
                name = "City1",
                temperature = 30.0,
                justAdded = true,
                isAlertNotified = false,
                pressure = 1013,
                temperatureMin = 30.0,
                temperatureMax = 30.0,
            )
            cityWeatherRepository.setCities(listOf(equalThresholdCity))

            useCase().test {
                val atThreshold = awaitItem()
                assertTrue(atThreshold.showAlert)
                assertTrue(atThreshold.isPersistentAlertActive)
                assertEquals(30.0, atThreshold.currentTemperature, 0.0)
                assertEquals(30.0, atThreshold.threshold, 0.0)

                cityWeatherRepository.setCities(
                    listOf(equalThresholdCity.copy(temperature = 35.0, temperatureMax = 40.0))
                )
                val aboveThreshold = awaitItem()
                assertTrue(aboveThreshold.showAlert)
                assertTrue(aboveThreshold.isPersistentAlertActive)
                assertEquals(35.0, aboveThreshold.currentTemperature, 0.0)

                cityWeatherRepository.updateAlertStatus(name = "City1", isNotified = true)
                val alreadyNotified = awaitItem()
                assertFalse(alreadyNotified.showAlert)
                assertTrue(alreadyNotified.isPersistentAlertActive)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `when temperature is below threshold emits inactive alert`() = runTest {
        cityWeatherRepository.setCities(
            listOf(
                CityWeatherDomain(
                    name = "City1",
                    temperature = 25.0,
                    justAdded = true,
                    isAlertNotified = false,
                    pressure = 1013,
                    temperatureMin = 20.0,
                    temperatureMax = 30.0,
                )
            )
        )

        useCase().test {
            val result = awaitItem()
            assertFalse(result.showAlert)
            assertFalse(result.isPersistentAlertActive)
            assertEquals(25.0, result.currentTemperature, 0.0)
            assertEquals(30.0, result.threshold, 0.0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when no city is justAdded emits empty alert result`() = runTest {
        cityWeatherRepository.setCities(
            listOf(
                CityWeatherDomain(
                    name = "City1",
                    temperature = 35.0,
                    justAdded = false,
                    isAlertNotified = false,
                    pressure = 1013,
                    temperatureMin = 30.0,
                    temperatureMax = 40.0,
                )
            )
        )

        useCase().test {
            val result = awaitItem()
            assertEquals(TemperatureAlertResult.EMPTY, result)
            assertFalse(result.showAlert)
            assertFalse(result.isPersistentAlertActive)
            assertEquals(0.0, result.currentTemperature, 0.0)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
