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

    /**
     * Arguments passed to a screen (FavoritesDetailViewModel in this case) are provided via its NavBackStackEntry,
     * which also defines the scope of the injected ViewModel. Therefore, the SavedStateHandle will contain
     * all arguments present in the NavBackStackEntry (refer to favoriteSubGraph for more details).
     */
    private val cityName =
        savedStateHandle.toRoute<FavoritesScreens.FavoritesDetail>().cityName
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val content: DetailContent = DetailContent.Loading
    )

    sealed interface DetailContent {
        data object Loading : DetailContent
        data class Success(val city: CityWeatherDomain?) : DetailContent
        data class Error(val error: CustomError) : DetailContent
    }

    fun onStart() {
        viewModelScope.launch {
            loadCityWeather()
        }
    }

    suspend fun loadCityWeather() {
        _state.update { it.copy(content = DetailContent.Loading) }

        val result =
            favoriteDetailInteractors.loadCityWeatherByNameUseCase.invoke(cityName = cityName)

        _state.update {
            it.copy(
                content = result.fold(
                    { error -> DetailContent.Error(error) },
                    { city -> DetailContent.Success(city) }
                )
            )
        }
    }
}
