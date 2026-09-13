package com.martorell.albert.meteomartocompose.ui.screens.favorites

import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.favorites.FavoritesInteractors
import com.martorell.albert.meteomartocompose.usecases.favorites.RemoveCityAsFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val getAllCitiesUseCase: GetAllCitiesUseCase = mockk()
    private val removeCityAsFavoriteUseCase: RemoveCityAsFavoriteUseCase = mockk()
    private val interactors = FavoritesInteractors(getAllCitiesUseCase, removeCityAsFavoriteUseCase)

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoritesViewModel(interactors)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and passive`() = runTest {
        val state = viewModel.state.value
        assertTrue(state.content is FavoritesViewModel.FavoritesContent.Loading)
        coVerify(exactly = 0) { getAllCitiesUseCase() }
    }

    @Test
    fun `onStart triggers data loading and updates success state`() = runTest {
        val cities = listOf(
            CityWeatherDomain(name = "Barcelona", favorite = true, temperature = 20.0, temperatureMin = 15.0, temperatureMax = 25.0, pressure = 1012),
            CityWeatherDomain(name = "Madrid", favorite = false, temperature = 22.0, temperatureMin = 18.0, temperatureMax = 28.0, pressure = 1010)
        )
        coEvery { getAllCitiesUseCase() } returns flowOf(cities)

        viewModel.onStart()

        val state = viewModel.state.value
        assertTrue(state.content is FavoritesViewModel.FavoritesContent.Success)
        val successContent = state.content as FavoritesViewModel.FavoritesContent.Success
        assertEquals(1, successContent.cities.size)
        assertEquals("Barcelona", successContent.cities[0].name)
        coVerify { getAllCitiesUseCase() }
    }

    @Test
    fun `removeCityFromFavorites calls use case and clears selection`() = runTest {
        coEvery { removeCityAsFavoriteUseCase(any()) } returns Unit
        
        viewModel.userClickedOnDeleteFavoriteCity("Barcelona")
        assertEquals("Barcelona", viewModel.state.value.cityToUnMarkAsFavorite)

        viewModel.removeCityFromFavorites()

        coVerify { removeCityAsFavoriteUseCase("Barcelona") }
        assertEquals("", viewModel.state.value.cityToUnMarkAsFavorite)
    }
}
