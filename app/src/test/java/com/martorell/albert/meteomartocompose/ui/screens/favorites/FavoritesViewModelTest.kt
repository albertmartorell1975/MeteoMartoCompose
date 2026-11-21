package com.martorell.albert.meteomartocompose.ui.screens.favorites

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.testutil.MainDispatcherRule
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeCityWeatherRepository
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.favorites.FavoritesInteractors
import com.martorell.albert.meteomartocompose.usecases.favorites.RemoveCityAsFavoriteUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cityWeatherRepository = FakeCityWeatherRepository()
    private val viewModel = FavoritesViewModel(
        FavoritesInteractors(
            getAllCitiesUseCase = GetAllCitiesUseCase(cityWeatherRepository),
            removeCityAsFavoriteUseCase = RemoveCityAsFavoriteUseCase(cityWeatherRepository),
        )
    )

    @Test
    fun `initial state is Loading and passive before onStart`() = runTest {
        assertTrue(viewModel.state.value.content is FavoritesViewModel.FavoritesContent.Loading)
        assertEquals(0, cityWeatherRepository.listOfCitiesAccessCount)
    }

    @Test
    fun `onStart filters favorite cities into Success state`() = runTest {
        cityWeatherRepository.setCities(
            listOf(
                CityWeatherDomain(
                    name = "Barcelona",
                    favorite = true,
                    temperature = 20.0,
                    temperatureMin = 15.0,
                    temperatureMax = 25.0,
                    pressure = 1012,
                ),
                CityWeatherDomain(
                    name = "Madrid",
                    favorite = false,
                    temperature = 22.0,
                    temperatureMin = 18.0,
                    temperatureMax = 28.0,
                    pressure = 1010,
                ),
            )
        )

        viewModel.onStart()

        val success = viewModel.state.value.content as FavoritesViewModel.FavoritesContent.Success
        assertEquals(listOf("Barcelona"), success.cities.map { it.name })
        assertEquals(1, cityWeatherRepository.listOfCitiesAccessCount)
    }

    @Test
    fun `removeCityFromFavorites unmarks selected city and clears pending selection`() = runTest {
        viewModel.userClickedOnDeleteFavoriteCity("Barcelona")
        assertEquals("Barcelona", viewModel.state.value.cityToUnMarkAsFavorite)

        viewModel.removeCityFromFavorites()

        assertEquals(listOf("Barcelona"), cityWeatherRepository.removedFavoriteCityNames)
        assertEquals("", viewModel.state.value.cityToUnMarkAsFavorite)
    }
}
