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
        val loading: Boolean = false,
        val error: CustomErrorFlow? = null,
        val citiesFavorites: List<CityWeatherDomain> = emptyList(),
        val cityToUnMarkAsFavorite: String = ""
    )

    init {

        viewModelScope.launch {
            getAllFavoritesCities()
        }

    }

    private suspend fun getAllFavoritesCities() {

        _state.update { stateUpdated ->
            stateUpdated.copy(
                loading = true
            )
        }

        favoritesInteractors.getAllCitiesUseCase.invoke()
            .catch { cause ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        loading = false,
                        error = cause.toCustomErrorFlow(),
                        citiesFavorites = emptyList()
                    )
                }
            }
            .collect { listOfCities ->
                _state.update { stateUpdated ->
                    stateUpdated.copy(
                        loading = false,
                        error = null,
                        citiesFavorites = listOfCities.filter { it.favorite }
                    )
                }

            }
    }

    suspend fun removeCityFromFavorites() {

        favoritesInteractors.removeCityAsFavoriteUseCase.invoke(_state.value.cityToUnMarkAsFavorite)

        _state.update { stateUpdated ->
            stateUpdated.copy(
                cityToUnMarkAsFavorite = ""
            )
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

    override fun onCleared() {
        super.onCleared()
    }

}