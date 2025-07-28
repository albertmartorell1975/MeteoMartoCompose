package com.martorell.albert.meteomartocompose.di.favoritedetail

import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherByNameUseCase
import com.martorell.albert.meteomartocompose.usecases.favoritedetail.FavoriteDetailInteractors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FavoriteDetailModule {

    @Provides
    fun favoriteDetailInteractorsProvider(
        loadCityWeatherByNameUseCase: LoadCityWeatherByNameUseCase
    ) = FavoriteDetailInteractors(
        loadCityWeatherByNameUseCase = loadCityWeatherByNameUseCase
    )

}