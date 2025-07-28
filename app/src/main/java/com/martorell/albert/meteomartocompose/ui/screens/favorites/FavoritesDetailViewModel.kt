package com.martorell.albert.meteomartocompose.ui.screens.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.ui.navigation.FavoritesScreens
import com.martorell.albert.meteomartocompose.usecases.favoritedetail.FavoriteDetailInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val favoriteDetailInteractors: FavoriteDetailInteractors
) : ViewModel() {

    /*Els arguments que passem a una pantalla, en aquest cas FavoritesDetailViewModel, es passen a través del seu navBackStackEntry el qual
   també defeinx el scope del viewmodel que injectem a  la pantalla. Per tant, a través del savedstatehandle se li passarà
   al ViewModel tots els arguments que estan en el navBackStackEntry (per més info anar a favoriteSubGraph)
    */
    private val cityName =
        savedStateHandle.toRoute<FavoritesScreens.FavoritesDetail>().cityName
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val error: CustomError? = null,
        val city: CityWeatherDomain? = null
    )

    init {

        viewModelScope.launch {

            loadCityWeather()

        }

    }

    private suspend fun loadCityWeather() {

        _state.update {
            it.copy(
                loading = true
            )
        }

        val result =
            favoriteDetailInteractors.loadCityWeatherByNameUseCase.invoke(cityName = cityName)

        _state.update {
            it.copy(
                loading = false,
                city = result
            )
        }

    }

    override fun onCleared() {

        super.onCleared()

    }

}