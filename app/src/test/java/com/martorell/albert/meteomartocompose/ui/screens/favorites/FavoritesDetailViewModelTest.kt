package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.lifecycle.SavedStateHandle
import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByNameUseCase
import com.martorell.albert.meteomartocompose.usecases.favoritedetail.FavoriteDetailInteractors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class FavoritesDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val loadCityWeatherByNameUseCase: LoadCityWeatherByNameUseCase = mockk()
    private val interactors = FavoriteDetailInteractors(loadCityWeatherByNameUseCase)
    
    private val savedStateHandle = SavedStateHandle(mapOf("cityName" to "Barcelona"))
    private lateinit var viewModel: FavoritesDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoritesDetailViewModel(savedStateHandle, interactors)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and passive`() = runTest {
        val state = viewModel.state.value
        assertTrue(state.content is FavoritesDetailViewModel.DetailContent.Loading)
        coVerify(exactly = 0) { loadCityWeatherByNameUseCase(any()) }
    }

    @Test
    fun `onStart triggers data loading and updates success state`() = runTest {
        val dummyCity = CityWeatherDomain(
            name = "Barcelona",
            temperature = 22.5,
            temperatureMin = 18.0,
            temperatureMax = 28.0,
            pressure = 1015
        )
        coEvery { loadCityWeatherByNameUseCase(cityName = "Barcelona") } returns dummyCity.right()

        viewModel.onStart()

        val state = viewModel.state.value
        assertTrue(state.content is FavoritesDetailViewModel.DetailContent.Success)
        assertEquals(dummyCity, (state.content as FavoritesDetailViewModel.DetailContent.Success).city)
    }

    @Test
    fun `onStart updates error state when use case fails`() = runTest {
        val error = CustomError.Connectivity
        coEvery { loadCityWeatherByNameUseCase(cityName = "Barcelona") } returns error.left()

        viewModel.onStart()

        val state = viewModel.state.value
        assertTrue(state.content is FavoritesDetailViewModel.DetailContent.Error)
        assertEquals(error, (state.content as FavoritesDetailViewModel.DetailContent.Error).error)
    }
}
