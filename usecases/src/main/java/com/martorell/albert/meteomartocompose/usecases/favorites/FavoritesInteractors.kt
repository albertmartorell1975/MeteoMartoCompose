package com.martorell.albert.meteomartocompose.usecases.favorites

import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByNameUseCase

data class FavoritesInteractors(
    val getAllCitiesUseCase: GetAllCitiesUseCase,
    val removeCityAsFavoriteUseCase: RemoveCityAsFavoriteUseCase,
    val loadCityWeatherByNameUseCase: LoadCityWeatherByNameUseCase
)