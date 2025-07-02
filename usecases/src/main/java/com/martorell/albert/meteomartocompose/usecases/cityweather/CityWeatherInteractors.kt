package com.martorell.albert.meteomartocompose.usecases.cityweather

data class CityWeatherInteractors(
    val checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase,
    val currentLocationUseCase: CurrentLocationUseCase,
    val isGPSEnableUseCase: IsGPSEnableUseCase,
    val loadCityForecastUseCase: LoadCityWeatherUseCase,
    val switchFavoriteUseCase: SwitchFavoriteUseCase,
    val getAllCitiesUseCase: GetAllCitiesUseCase,
    val isCurrentCityFavoriteUseCase: IsCurrentCityFavoriteUseCase
)