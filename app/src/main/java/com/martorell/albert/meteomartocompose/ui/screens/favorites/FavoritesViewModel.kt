package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.data.CustomErrorFlow
import com.martorell.albert.meteomartocompose.data.toCustomErrorFlow
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.usecases.favorites.FavoritesInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesInteractors: FavoritesInteractors
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val content: FavoritesContent = FavoritesContent.Loading,
        val cityToUnMarkAsFavorite: String = ""
    )

    sealed interface FavoritesContent {
        data object Loading : FavoritesContent
        data class Success(val cities: List<CityWeatherDomain>) : FavoritesContent
        data class Error(val error: CustomErrorFlow) : FavoritesContent
    }

    fun onStart() {
        viewModelScope.launch {
            getAllFavoritesCities()
        }
    }

    private suspend fun getAllFavoritesCities() {
        _state.update { it.copy(content = FavoritesContent.Loading) }

        favoritesInteractors.getAllCitiesUseCase.invoke()
            .catch { cause ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        content = FavoritesContent.Error(cause.toCustomErrorFlow())
                    )
                }
            }
            .collect { listOfCities ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        content = FavoritesContent.Success(listOfCities.filter { it.favorite })
                    )
                }
            }
    }

    fun removeCityFromFavorites() {
        viewModelScope.launch {
            favoritesInteractors.removeCityAsFavoriteUseCase.invoke(_state.value.cityToUnMarkAsFavorite)

            _state.update { stateUpdated ->
                stateUpdated.copy(
                    cityToUnMarkAsFavorite = ""
                )
            }
        }
    }

    fun userClickedOnDeleteFavoriteCity(cityName: String) {

        _state.update { stateUpdated ->
            stateUpdated.copy(
                cityToUnMarkAsFavorite = cityName
            )
        }

    }

    fun userDismissedAlertDialog() {

        _state.update {
            it.copy(
                cityToUnMarkAsFavorite = ""
            )
        }

    }

}