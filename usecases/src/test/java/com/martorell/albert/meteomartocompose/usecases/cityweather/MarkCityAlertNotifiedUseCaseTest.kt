package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MarkCityAlertNotifiedUseCaseTest {

    private val cityWeatherRepository: CityWeatherRepository = mockk()
    private val useCase = MarkCityAlertNotifiedUseCase(cityWeatherRepository)

    @Test
    fun `when invoke, should call repository updateAlertStatus`() = runTest {
        // Given
        val cityName = "Barcelona"
        val notified = true
        coEvery { cityWeatherRepository.updateAlertStatus(cityName, notified) } just runs

        // When
        useCase(cityName, notified)

        // Then
        coVerify { cityWeatherRepository.updateAlertStatus(cityName, notified) }
    }
}
