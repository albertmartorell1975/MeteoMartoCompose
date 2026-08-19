package com.martorell.albert.meteomartocompose.di.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.CityWeatherRepository
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationServerDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.CityRoomDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.CityWeatherRepositoryImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationRepositoryImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationRoomDataImpl
import com.martorell.albert.meteomartocompose.data.cityweather.LocationServerDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CityWeatherBindingModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindLocationServerDataSource(
        locationServerDataSourceImpl: LocationServerDataSourceImpl
    ): LocationServerDataSource

    @Binds
    @Singleton
    abstract fun bindLocationLocalDataSource(
        locationRoomDataImpl: LocationRoomDataImpl
    ): LocationLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCityLocalDataSource(
        cityRoomDataSource: CityRoomDataSource
    ): CityWeatherLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCityWeatherRepository(
        cityWeatherRepositoryImpl: CityWeatherRepositoryImpl
    ): CityWeatherRepository
}
