package com.martorell.albert.meteomartocompose.di.cityweather

import com.google.android.gms.location.FusedLocationProviderClient
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationServerDataSource
import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.CityRoomDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.CityWeatherRepositoryImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationRepositoryImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationRoomDataImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationServerDataSourceImpl
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CheckLocationPermissionsUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.CityWeatherInteractors
import com.martorell.albert.meteomartocompose.usecases.cityweather.CurrentLocationUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.GetAllCitiesUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.IsCurrentCityFavoriteUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.IsGPSEnableUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.LoadCityWeatherUseCase
import com.martorell.albert.meteomartocompose.usecases.cityweather.SwitchFavoriteUseCase
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
        isGPSEnableUseCase: IsGPSEnableUseCase,
        loadCityWeatherUseCase: LoadCityWeatherUseCase,
        switchFavoriteUseCase: SwitchFavoriteUseCase,
        getAllCitiesUseCase: GetAllCitiesUseCase,
        isCurrentCityFavoriteUseCase: IsCurrentCityFavoriteUseCase
    ) = CityWeatherInteractors(
        checkLocationPermissionsUseCase = checkLocationPermissionsUseCase,
        currentLocationUseCase = currentLocationUseCase,
        isGPSEnableUseCase = isGPSEnableUseCase,
        loadCityForecastUseCase = loadCityWeatherUseCase,
        switchFavoriteUseCase = switchFavoriteUseCase,
        getAllCitiesUseCase = getAllCitiesUseCase,
        isCurrentCityFavoriteUseCase = isCurrentCityFavoriteUseCase
    )

    @Provides
    fun providesLocationRepository(
        locationServerDataSource: LocationServerDataSource,
        locationLocalDataSource: LocationLocalDataSource
    ): LocationRepository {
        return LocationRepositoryImpl(
            locationServerDataSource = locationServerDataSource,
            locationLocalDataSource = locationLocalDataSource
        )
    }

    @Provides
    fun providesLocationServerDataSource(fusedLocationProviderClient: FusedLocationProviderClient): LocationServerDataSource {
        return LocationServerDataSourceImpl(fusedLocationProviderClient)
    }

    @Provides
    fun providesLocationLocalDataSource(db: MeteoMartoDatabase): LocationLocalDataSource =
        LocationRoomDataImpl(db)

    @Provides
    fun providesCityLocalDataSource(db: MeteoMartoDatabase): CityWeatherLocalDataSource =
        CityRoomDataSource(db)

    @Provides
    fun providesCityWeatherRepository(
        cityWeatherService: CityWeatherServerDataSource,
        cityLocalDataSource: CityWeatherLocalDataSource
    ): CityWeatherRepository {
        return CityWeatherRepositoryImpl(
            cityWeatherServerDataSource = cityWeatherService,
            cityWeatherLocalDataSource = cityLocalDataSource
        )
    }

}