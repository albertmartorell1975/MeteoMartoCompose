package com.martorell.albert.meteomartocompose.di.favorites

import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByNameUseCase
import com.martorell.albert.meteomartocompose.usecases.favorites.FavoritesInteractors
import com.martorell.albert.meteomartocompose.usecases.favorites.RemoveCityAsFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FavoritesModule {

    @Provides
    fun favoritesInteractorsProvider(
        getAllCitiesUseCase: GetAllCitiesUseCase,
        removeCityAsFavoriteUseCase: RemoveCityAsFavoriteUseCase,
        loadCityWeatherByNameUseCase: LoadCityWeatherByNameUseCase
    ) = FavoritesInteractors(
        getAllCitiesUseCase = getAllCitiesUseCase,
        removeCityAsFavoriteUseCase = removeCityAsFavoriteUseCase,
        loadCityWeatherByNameUseCase = loadCityWeatherByNameUseCase
    )
}