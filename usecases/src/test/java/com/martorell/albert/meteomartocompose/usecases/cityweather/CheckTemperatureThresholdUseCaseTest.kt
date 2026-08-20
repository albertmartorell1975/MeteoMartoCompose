package com.martorell.albert.meteomartocompose.usecases.cityweather

import app.cash.turbine.test
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckTemperatureThresholdUseCaseTest {

    private val cityWeatherRepository: CityWeatherRepository = mockk()
    private val remoteConfigRepository: RemoteConfigRepository = mockk()
    private val useCase = CheckTemperatureThresholdUseCase(cityWeatherRepository, remoteConfigRepository)

    @Test
    fun `when temperature exceeds threshold and not notified, showAlert should be true`() = runTest {
        // Given
        val threshold = 30.0
        val currentTemp = 35.0
        val cities = listOf(
            CityWeatherDomain(name = "City1", temperature = currentTemp, justAdded = true, isAlertNotified = false, pressure = 1013, temperatureMin = 30.0, temperatureMax = 40.0)
        )
        every { remoteConfigRepository.getTemperatureThreshold() } returns flowOf(threshold)
        every { cityWeatherRepository.listOfCities } returns flowOf(cities)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.showAlert)
            assertTrue(result.isPersistentAlertActive)
            assertEquals(currentTemp, result.currentTemperature, 0.0)
            assertEquals(threshold, result.threshold, 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `when temperature exceeds threshold but already notified, showAlert should be false`() = runTest {
        // Given
        val threshold = 30.0
        val currentTemp = 35.0
        val cities = listOf(
            CityWeatherDomain(name = "City1", temperature = currentTemp, justAdded = true, isAlertNotified = true, pressure = 1013, temperatureMin = 30.0, temperatureMax = 40.0)
        )
        every { remoteConfigRepository.getTemperatureThreshold() } returns flowOf(threshold)
        every { cityWeatherRepository.listOfCities } returns flowOf(cities)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertFalse(result.showAlert)
            assertTrue(result.isPersistentAlertActive)
            awaitComplete()
        }
    }

    @Test
    fun `when temperature is below threshold, showAlert and persistent alert should be false`() = runTest {
        // Given
        val threshold = 30.0
        val currentTemp = 25.0
        val cities = listOf(
            CityWeatherDomain(name = "City1", temperature = currentTemp, justAdded = true, isAlertNotified = false, pressure = 1013, temperatureMin = 20.0, temperatureMax = 30.0)
        )
        every { remoteConfigRepository.getTemperatureThreshold() } returns flowOf(threshold)
        every { cityWeatherRepository.listOfCities } returns flowOf(cities)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertFalse(result.showAlert)
            assertFalse(result.isPersistentAlertActive)
            assertEquals(currentTemp, result.currentTemperature, 0.0)
            assertEquals(threshold, result.threshold, 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `when no city is justAdded, temperature should be 0 and showAlert false`() = runTest {
        // Given
        val threshold = 30.0
        val cities = listOf(
            CityWeatherDomain(name = "City1", temperature = 35.0, justAdded = false, isAlertNotified = false, pressure = 1013, temperatureMin = 30.0, temperatureMax = 40.0)
        )
        every { remoteConfigRepository.getTemperatureThreshold() } returns flowOf(threshold)
        every { cityWeatherRepository.listOfCities } returns flowOf(cities)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertFalse(result.showAlert)
            assertFalse(result.isPersistentAlertActive)
            assertEquals(0.0, result.currentTemperature, 0.0)
            awaitComplete()
        }
    }
}
