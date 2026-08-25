package com.martorell.albert.meteomartocompose.usecases.cityweather

import javax.inject.Inject

data class CityWeatherInteractors @Inject constructor(
    val checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase,
    val checkNotificationPermissionUseCase: CheckNotificationPermissionUseCase,
    val currentLocationUseCase: CurrentLocationUseCase,
    val isGPSEnableUseCase: IsGPSEnableUseCase,
    val loadCityWeatherByCoordinatesUseCase: LoadCityWeatherByCoordinatesUseCase,
    val switchFavoriteUseCase: SwitchFavoriteUseCase,
    val getAllCitiesUseCase: GetAllCitiesUseCase,
    val isCurrentCityFavoriteUseCase: IsCurrentCityFavoriteUseCase,
    val logOutUseCase: LogOutUseCase,
    val saveLocationUseCase: SaveLocationUseCase,
    val checkTemperatureThresholdUseCase: CheckTemperatureThresholdUseCase,
    val markCityAlertNotifiedUseCase: MarkCityAlertNotifiedUseCase,
    val getWeatherPermissionsUseCase: GetWeatherPermissionsUseCase,
)
