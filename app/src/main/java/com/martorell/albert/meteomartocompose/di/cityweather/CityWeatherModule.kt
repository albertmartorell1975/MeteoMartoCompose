package com.martorell.albert.meteomartocompose.di.cityweather

import com.google.android.gms.location.FusedLocationProviderClient
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.LocationDataSourceImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationRepositoryImpl
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckLocationPermissionsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import com.martorell.albert.meteomartocompose.usecases.cityweather.CurrentLocationUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.IsGPSEnableUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CityWeatherModule {

    @Provides
    fun cityWeatherInteractorsProvider(
        checkLocationPermissionsUseCase: CheckLocationPermissionsUseCase,
        currentLocationUseCase: CurrentLocationUseCase,
        isGPSEnableUseCase: IsGPSEnableUseCase
    ) = CityWeatherInteractors(
        checkLocationPermissionsUseCase = checkLocationPermissionsUseCase,
        currentLocationUseCase = currentLocationUseCase,
        isGPSEnableUseCase = isGPSEnableUseCase
    )

    @Provides
    fun providesLocationRepository(
        locationDataSource: LocationDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(
            locationDataSource = locationDataSource
        )
    }

    @Provides
    fun providesLocationDataSource(fusedLocationProviderClient: FusedLocationProviderClient): LocationDataSource {
        return LocationDataSourceImpl(fusedLocationProviderClient)
    }

}