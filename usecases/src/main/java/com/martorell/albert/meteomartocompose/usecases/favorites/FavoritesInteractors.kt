package com.martorell.albert.meteomartocompose.usecases.favorites

import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase

data class FavoritesInteractors(
    val getAllCitiesUseCase: GetAllCitiesUseCase,
    val removeCityAsFavoriteUseCase: RemoveCityAsFavoriteUseCase
)