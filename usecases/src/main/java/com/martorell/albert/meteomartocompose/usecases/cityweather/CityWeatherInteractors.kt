package com.martorell.albert.meteomartocompose.usecases.cityweather

data class CityWeatherInteractors(
    val checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase,
    val currentLocationUseCase: CurrentLocationUseCase,
    val isGPSEnableUseCase: IsGPSEnableUseCase
)