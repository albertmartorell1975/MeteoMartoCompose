package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.lifecycle.SavedStateHandle
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.testutil.MainDispatcherRule
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeCityWeatherRepository
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByNameUseCase
import com.martorell.albert.meteomartocompose.usecases.favoritedetail.FavoriteDetailInteractors
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoritesDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val cityWeatherRepository = FakeCityWeatherRepository()
    private val viewModel = FavoritesDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf(FavoritesDetailViewModel.CITY_NAME_ARG to "Barcelona")),
        favoriteDetailInteractors = FavoriteDetailInteractors(
            loadCityWeatherByNameUseCase = LoadCityWeatherByNameUseCase(cityWeatherRepository),
        ),
    )

    @Test
    fun `initial state is Loading and passive before onStart`() = runTest {
        assertTrue(viewModel.state.value.content is FavoritesDetailViewModel.DetailContent.Loading)
        assertTrue(cityWeatherRepository.loadCityByNameCalls.isEmpty())
    }

    @Test
    fun `onStart loads city weather into Success state`() = runTest {
        val dummyCity = CityWeatherDomain(
            name = "Barcelona",
            temperature = 22.5,
            temperatureMin = 18.0,
            temperatureMax = 28.0,
            pressure = 1015,
        )
        cityWeatherRepository.loadCityByNameResult = Result.Success(dummyCity)

        viewModel.onStart()

        val success = viewModel.state.value.content as FavoritesDetailViewModel.DetailContent.Success
        assertEquals(dummyCity, success.city)
        assertEquals(listOf("Barcelona"), cityWeatherRepository.loadCityByNameCalls)
    }

    @Test
    fun `onStart updates Error state when repository fails`() = runTest {
        val error = DomainError.Network.NoInternet
        cityWeatherRepository.loadCityByNameResult = Result.Error(error)

        viewModel.onStart()

        val errorContent = viewModel.state.value.content as FavoritesDetailViewModel.DetailContent.Error
        assertEquals(error, errorContent.error)
        assertEquals(listOf("Barcelona"), cityWeatherRepository.loadCityByNameCalls)
    }
}
