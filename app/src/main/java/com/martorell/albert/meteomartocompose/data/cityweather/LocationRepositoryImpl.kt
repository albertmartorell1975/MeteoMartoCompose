package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationServerDataSource
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import jakarta.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationServerDataSource: LocationServerDataSource,
    private val locationLocalDataSource: LocationLocalDataSource
) :
    LocationRepository {

    override suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain> =
        locationServerDataSource.loadCurrentLocation()

    override suspend fun saveLocation(latitude: Double?, longitude: Double?) {

        locationLocalDataSource.saveLocation(
            latitude = latitude,
            longitude = longitude
        )

    }

}